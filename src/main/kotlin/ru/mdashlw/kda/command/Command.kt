package ru.mdashlw.kda.command

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.requests.restaction.MessageAction
import ru.mdashlw.kda.builder.impl.EmbedBuilder
import ru.mdashlw.kda.command.categories.MainCategory
import ru.mdashlw.kda.command.manager.CommandManager
import ru.mdashlw.kda.dsl.KdaDslMarker
import ru.mdashlw.kda.pagination.Pagination
import java.time.Duration
import java.util.*

@KdaDslMarker
class Command(val parent: Command? = null) {
    var name: String = ""
        set(value) {
            field = value
            qualifiedName = parent?.let { "${it.name} $value" } ?: value
            usage = ""
        }
    var category: Category = parent?.category ?: MainCategory
        set(value) {
            field = value
            memberPermissions = category.memberPermissions
            selfPermissions = category.selfPermissions
            displayInHelp = category.displayInHelp
            access = category.access
        }
    var description: String = "<no description>"
    var usage: String = ""
        set(value) {
            field = "$qualifiedName $value".trim()
        }
    var aliases: List<String> = emptyList()
    var hiddenAliases: List<String> = emptyList()
    var examples: List<String> = emptyList()
        set(value) {
            field = value.map { "$qualifiedName $it".trim() }
        }
    var memberPermissions: EnumSet<Permission> = category.memberPermissions
    var selfPermissions: EnumSet<Permission> = category.selfPermissions
    var displayInHelp: Boolean = category.displayInHelp
    var access: Context.() -> Boolean = category.access

    var qualifiedName: String = ""

    val commands = mutableMapOf<String, Command>()
    val actions = mutableListOf<Action>()

    class Action(
        val minArgs: Int,
        val maxArgs: Int,
        val typing: Boolean,
        val action: Context.() -> Unit
    )

    inner class Context(
        val jda: JDA,
        val guild: Guild,
        val guildSettings: GuildSettings,
        val channel: TextChannel,
        val user: User,
        val member: Member,
        val message: Message,
        var args: List<String>
    ) {
        var index: Int = -1

        fun take(amount: Int): String? =
            args
                .take(amount)
                .joinToString(" ")
                .takeIf(String::isNotEmpty)
                .also {
                    index++
                    args = args.drop(amount)
                }

        fun reply(text: CharSequence): MessageAction = channel.sendMessage(text)

        fun reply(embed: MessageEmbed): MessageAction = channel.sendMessage(embed)

        fun reply(block: EmbedBuilder.() -> Unit): MessageAction {
            val builder = EmbedBuilder()

            CommandManager.replyModifiers
                .filter { it.check(this@Command, this) }
                .forEach {
                    builder.run(it.modify(this@Command, this))
                }

            return reply(builder.apply(block).build())
        }

        fun <T> replyPagination(
            content: Collection<T>,
            timeout: Duration = Duration.ofMinutes(10),
            itemsPerPage: Int = 15,
            displayFooter: Boolean = true,
            provider: EmbedBuilder.(Collection<T>) -> Unit
        ) {
            Pagination(
                content,
                jda,
                channel.idLong,
                setOf(user.idLong),
                timeout,
                itemsPerPage,
                displayFooter
            ) { collection ->
                CommandManager.replyModifiers
                    .filter { it.check(this@Command, this@Context) }
                    .forEach {
                        run(it.modify(this@Command, this@Context))
                    }

                provider(this, collection)
            }.init()
        }

        fun replyHelp(): MessageAction = CommandManager.replies.help(this@Command, this)

        fun replySuccess(message: String): MessageAction = CommandManager.replies.success(this@Command, this, message)

        fun replyError(message: String): MessageAction = CommandManager.replies.error(this@Command, this, message)

        fun error(message: String): Nothing = throw Error(message)

        fun copy(command: Command, args: List<String> = this.args) =
            command.Context(jda, guild, guildSettings, channel, user, member, message, args)
    }

    class Help : RuntimeException()

    class Error(override val message: String) : RuntimeException()
}
