package ru.mdashlw.kda.command.client

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.colors.Colors
import ru.mdashlw.kda.command.context.CommandContext
import ru.mdashlw.kda.command.context.impl.*
import ru.mdashlw.kda.command.context.register
import ru.mdashlw.kda.command.emotes.Emotes
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
import ru.mdashlw.util.string.removeExtraSpaces
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KClass

class CommandClient(
    val owner: Long,
    val prefix: String,
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

    @Suppress("NAME_SHADOWING")
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

        val channel = event.channel
        val message = event.message
        content = content.substring(guildSettings.prefix.length).trim()
        val args = content.split(" ")

        val command = findCommand(args[0]) ?: return
        val event = command.Event(event.jda, guild, guildSettings, channel, member, message)

        GlobalScope.launch(executor) {
            CommandHandler.handle(command, event, args.drop(1))
        }
    }

    companion object {
        // TODO Change it somehow
        lateinit var INSTANCE: CommandClient
            private set
    }
}
