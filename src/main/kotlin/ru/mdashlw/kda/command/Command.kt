package ru.mdashlw.kda.command

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.requests.restaction.MessageAction
import ru.mdashlw.kda.builder.impl.EmbedBuilder
import ru.mdashlw.kda.command.annotations.CommandFunction
import ru.mdashlw.kda.command.client.CommandClient
import ru.mdashlw.kda.command.guildsettings.GuildSettings
import ru.mdashlw.kda.pagination.Pagination
import java.time.Duration
import java.util.*
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions

abstract class Command {
    abstract val name: String
    open val aliases: List<String>? = null
    open val description: String = "<no description provided>"
    open var usage: String = ""
    open var examples: List<String>? = null
    open var memberPermissions: List<Permission>? = null
    open var selfPermissions: List<Permission>? = null
    open val displayInHelp: Boolean = true
    open val sendTyping: Boolean = false

    @Suppress("UNCHECKED_CAST")
    val functions: List<KFunction<Unit>> =
        javaClass.kotlin.functions
            .filter { it.findAnnotation<CommandFunction>() != null }
            .map { it as KFunction<Unit> }
            .sortedByDescending { it.parameters.size }

    val subCommands = mutableMapOf<String, SubCommand>()

    protected open fun fixMeta() {
        usage = (name + usage).trim()
        examples = examples?.map { (name + it).trim() }
    }

    open fun register() {
        fixMeta()

        CommandClient.INSTANCE.commands[name.toLowerCase()] = this
        aliases?.forEach { CommandClient.INSTANCE.commands[it.toLowerCase()] = this }
    }

    open fun checkAccess(event: Event): Boolean = true

    inner class Event(
        val api: JDA,
        val guild: Guild,
        val user: User,
        val member: Member,
        val channel: TextChannel,
        val message: Message,
        val guildSettings: GuildSettings,
        val prefix: String,
        val localization: ResourceBundle
    ) {
        fun localize(key: String, vararg parameters: Any): String {
            var string = localization.getString(key) ?: key

            parameters.forEachIndexed { index, value ->
                string = string.replace("{$index}", value.toString())
            }

            return string
        }

        fun reply(text: String): MessageAction =
            channel.sendMessage(text)

        fun reply(embed: MessageEmbed): MessageAction =
            channel.sendMessage(embed)

        fun reply(block: EmbedBuilder.() -> Unit): MessageAction {
            val builder = EmbedBuilder()

            CommandClient.INSTANCE.replyModifiers
                .filter { it.check(this@Command, this@Event) }
                .forEach {
                    builder.run(it.modify(this@Command, this@Event))
                }

            return reply(builder.apply(block).build())
        }

        fun <T> replyPagination(
            content: Collection<T>,
            timeout: Duration = Duration.ofMinutes(10),
            itemsOnPage: Int = 15,
            block: EmbedBuilder.(Collection<T>) -> Unit
        ) {
            Pagination(
                api, channel.idLong, setOf(user.idLong), timeout, itemsOnPage, content
            ) {
                CommandClient.INSTANCE.replyModifiers
                    .filter { it.check(this@Command, this@Event) }
                    .forEach {
                        run(it.modify(this@Command, this@Event))
                    }

                block(it)
            }.display()
        }

        fun replyHelp(): MessageAction =
            CommandClient.INSTANCE.replies.help(this@Command, this@Event)

        fun replySuccess(message: String): MessageAction =
            CommandClient.INSTANCE.replies.success(this@Command, this@Event, message)

        fun replyWarning(message: String): MessageAction =
            CommandClient.INSTANCE.replies.warning(this@Command, this@Event, message)

        fun replyError(message: String): MessageAction =
            CommandClient.INSTANCE.replies.error(this@Command, this@Event, message)

        fun copy(command: Command): Event =
            command.Event(api, guild, user, member, channel, message, guildSettings, prefix, localization)
    }

    class Help : Exception()

    class Success(message: String) : Exception(message)

    class Warning(message: String) : Exception(message)

    class Error(message: String) : Exception(message)
}
