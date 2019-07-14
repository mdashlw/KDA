package ru.mdashlw.kda.command.manager

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import ru.mdashlw.kda.command.*
import ru.mdashlw.kda.command.colors.DefaultColors
import ru.mdashlw.kda.command.emotes.DefaultEmotes
import ru.mdashlw.kda.command.exceptionhandlers.*
import ru.mdashlw.kda.command.exceptions.NoAccessException
import ru.mdashlw.kda.command.exceptions.NoMemberPermissionsException
import ru.mdashlw.kda.command.exceptions.NoSelfPermissionsException
import ru.mdashlw.kda.command.guildsettings.EmptyGuildSettings
import ru.mdashlw.kda.command.replies.DefaultReplies
import ru.mdashlw.kda.command.replymodifiers.ColorModifier
import ru.mdashlw.util.removeExtraSpaces
import ru.mdashlw.util.thread.CustomizableThreadFactory
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.full.isSuperclassOf

object CommandManager : ListenerAdapter() {
    var executor: CoroutineContext = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors(),
        CustomizableThreadFactory("KDA-Commands %d")
    ).asCoroutineDispatcher()
    lateinit var prefix: String
    var owner: Long = 0
    var guildSettingsProvider: GuildSettingsProvider? = null
    var uncaughtExceptionHandler: UncaughtExceptionHandler = DefaultUncaughtExceptionHandler
    var replies: Replies = DefaultReplies
    var colors: Colors = DefaultColors
    var emotes: Emotes = DefaultEmotes

    val commands = mutableMapOf<String, Command>()
    val exceptionHandlers = mutableListOf<ExceptionHandler<Throwable>>()
    val replyModifiers = mutableListOf<ReplyModifier>()

    init {
        registerDefaultExceptionHandlers()
        registerDefaultReplyModifiers()
    }

    private fun registerDefaultExceptionHandlers() {
        CommandHelpHandler.register()
        CommandErrorHandler.register()
        NoAccessHandler.register()
        NoMemberPermissionsHandler.register()
        NoSelfPermissionsHandler.register()
    }

    private fun registerDefaultReplyModifiers() {
        ColorModifier.register()
    }

    fun getCommand(name: String, parent: Command? = null): Command? =
        (parent?.commands ?: commands)[name.toLowerCase()]

    fun getCommandAction(command: Command, args: Int): Command.Action? =
        command.actions.find { args >= it.minArgs && (it.maxArgs == -1 || args <= it.maxArgs) }

    fun getExceptionHandler(exception: Throwable): ExceptionHandler<Throwable>? {
        val exceptionClass = exception::class

        return exceptionHandlers.find { it.type == exceptionClass }
            ?: exceptionHandlers.find { it.type.isSuperclassOf(exceptionClass) }
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        val jda = event.jda
        val guild = event.guild
        val channel = event.channel
        val user = event.author.takeUnless(User::isBot) ?: return
        val member = event.member ?: return
        val message = event.message
        var content = message.contentRaw.removeExtraSpaces()
        val guildSettings = guildSettingsProvider?.provide(guild) ?: EmptyGuildSettings
        val prefix = guildSettings.prefix
        val commandsChannel = guildSettings.channel

        if (!content.startsWith(prefix, true)) {
            return
        }

        content = content.substring(prefix.length).trim()

        var args = content.split(" ")
        val command = getCommand(args[0]) ?: return
        args = args.drop(1)
        val context = command.Context(jda, guild, guildSettings, channel, user, member, message, args)

        if (commandsChannel != -1L && channel.idLong != commandsChannel && !member.hasPermission(Permission.MANAGE_SERVER)) {
            replies.wrongChannel(command, context, guild.getTextChannelById(commandsChannel) ?: return).queue()
            return
        }

        GlobalScope.launch(executor) {
            execute(command, context, args)
        }
    }

    private fun execute(command: Command, context: Command.Context, args: List<String>) {
        try {
            if (!command.access(context) && context.user.idLong != owner) {
                throw NoAccessException()
            }

            if (!context.member.hasPermission(command.memberPermissions)) {
                throw NoMemberPermissionsException()
            }

            if (!context.guild.selfMember.hasPermission(command.selfPermissions)) {
                throw NoSelfPermissionsException()
            }

            if (args.isEmpty()) {
                return execute(command, context, 0)
            }

            val subCommand = getCommand(args[0], command)

            if (subCommand != null) {
                val newArgs = args.drop(1)

                return execute(
                    subCommand,
                    context.copy(subCommand, newArgs),
                    newArgs
                )
            }

            return execute(command, context, args.size)
        } catch (exception: Throwable) {
            val exceptionHandler = getExceptionHandler(exception)
                ?: uncaughtExceptionHandler

            exceptionHandler.handle(command, context, exception)
        }
    }

    private fun execute(command: Command, context: Command.Context, args: Int) =
        execute(
            context,
            getCommandAction(command, args)
                ?: throw Command.Help()
        )

    private fun execute(context: Command.Context, action: Command.Action) {
        if (action.typing) {
            context.channel.sendTyping().queue()
        }

        action.action(context)
    }
}
