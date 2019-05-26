package ru.mdashlw.kda.command.client

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.colors.Colors
import ru.mdashlw.kda.command.context.CommandContext
import ru.mdashlw.kda.command.context.impl.*
import ru.mdashlw.kda.command.context.register
import ru.mdashlw.kda.command.emotes.Emotes
import ru.mdashlw.kda.command.events.CommandInvokeEvent
import ru.mdashlw.kda.command.exceptionhandler.ExceptionHandler
import ru.mdashlw.kda.command.exceptionhandler.impl.*
import ru.mdashlw.kda.command.exceptionhandler.register
import ru.mdashlw.kda.command.exceptionhandler.uncaught.UncaughtExceptionHandler
import ru.mdashlw.kda.command.guildsettings.impl.EmptyGuildSettings
import ru.mdashlw.kda.command.guildsettings.provider.GuildSettingsProvider
import ru.mdashlw.kda.command.internal.findCommand
import ru.mdashlw.kda.command.internal.handler.CommandHandler
import ru.mdashlw.kda.command.replies.Replies
import ru.mdashlw.kda.command.replymodifier.ReplyModifier
import ru.mdashlw.kda.command.replymodifier.impl.ColorModifier
import ru.mdashlw.kda.command.replymodifier.register
import ru.mdashlw.util.removeExtraSpaces
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

class CommandClient(
    val owner: Long,
    val prefix: String,
    languages: List<Locale>,
    val requiresEmbedLinks: Boolean,
    val executor: CoroutineContext,
    val guildSettingsProvider: GuildSettingsProvider?,
    val uncaughtExceptionHandler: UncaughtExceptionHandler,
    val replies: Replies,
    val colors: Colors,
    val emotes: Emotes
) : ListenerAdapter() {
    val commands = mutableMapOf<String, Command>()
    val contexts = mutableMapOf<KClass<out Any>, CommandContext<Any>>()
    val exceptionHandlers = mutableMapOf<KClass<out Throwable>, ExceptionHandler<Throwable>>()
    val replyModifiers = mutableListOf<ReplyModifier>()

    val resourceBundles = languages.associateWith { ResourceBundle.getBundle("messages", it) }

    init {
        INSTANCE = this

        registerDefaultContexts()
        registerDefaultExceptionHandlers()
        registerDefaultReplyModifiers()
    }

    private fun registerDefaultContexts() {
        StringContext.register()
        IntContext.register()
        BooleanContext.register()
        LongContext.register()
        TextChannelContext.register()
        CommandCommandContext.register()
    }

    private fun registerDefaultExceptionHandlers() {
        CommandErrorHandler.register()
        CommandHelpHandler.register()
        CommandSuccessHandler.register()
        CommandWarningHandler.register()
        IllegalUsageHandler.register()
        NoAccessHandler.register()
        NoMemberPermissionsHandler.register()
        NoSelfPermissionsHandler.register()
    }

    private fun registerDefaultReplyModifiers() {
        ColorModifier.register()
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val author = event.author
        val member = event.member

        if (author.isBot || member == null) {
            return
        }

        val guild = event.guild
        var content = event.message.contentRaw.trim().removeExtraSpaces()
        val guildSettings = guildSettingsProvider?.provide(guild) ?: EmptyGuildSettings

        if (!content.startsWith(guildSettings.prefix, true)) {
            return
        }

        val jda = event.jda
        val channel = event.channel
        val message = event.message
        content = content.substring(guildSettings.prefix.length).trim()
        val args = content.split(' ')

        val command = findCommand(args[0]) ?: return
        val commandEvent = command.Event(
            jda,
            guild,
            author,
            member,
            channel,
            message,
            guildSettings,
            guildSettings.prefix,
            resourceBundles[guildSettings.locale] ?: error("No resource bundle for locale ${guildSettings.locale}")
        )

        jda.eventManager.handle(CommandInvokeEvent(jda, event.responseNumber, command, commandEvent, args))

        GlobalScope.launch(executor) {
            if (requiresEmbedLinks && !guild.selfMember.hasPermission(channel, Permission.MESSAGE_EMBED_LINKS)) {
                commandEvent.run {
                    reply(localize("replies.no_embed_links_permission")).queue()
                }

                return@launch
            }

            CommandHandler.handle(command, commandEvent, args.drop(1))
        }
    }

    companion object {
        lateinit var INSTANCE: CommandClient
            private set
    }
}
