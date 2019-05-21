package ru.mdashlw.kda.builder.impl

import kotlinx.coroutines.asCoroutineDispatcher
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.hooks.IEventManager
import net.dv8tion.jda.api.utils.cache.CacheFlag
import ru.mdashlw.kda.build
import ru.mdashlw.kda.builder.Builder
import ru.mdashlw.kda.command.client.CommandClient
import ru.mdashlw.kda.command.colors.Colors
import ru.mdashlw.kda.command.colors.impl.DefaultColors
import ru.mdashlw.kda.command.emotes.Emotes
import ru.mdashlw.kda.command.emotes.impl.DefaultEmotes
import ru.mdashlw.kda.command.exceptionhandler.uncaught.UncaughtExceptionHandler
import ru.mdashlw.kda.command.exceptionhandler.uncaught.impl.DefaultUncaughtExceptionHandler
import ru.mdashlw.kda.command.guildsettings.provider.GuildSettingsProvider
import ru.mdashlw.kda.command.replies.Replies
import ru.mdashlw.kda.command.replies.impl.DefaultReplies
import java.util.*
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class KdaBuilder : Builder<JDA>() {
    lateinit var token: String
    var cacheFlags: EnumSet<CacheFlag> = EnumSet.allOf(CacheFlag::class.java)
    var eventManager: IEventManager? = null
    var activity: Activity? = null
    var status: OnlineStatus = OnlineStatus.ONLINE
    var bulkDeleteSplitting: Boolean = true
    var idle: Boolean = false

    var commandClient: CommandClient? = null

    inline fun command(block: CommandClientBuilder.() -> Unit) {
        commandClient = build(CommandClientBuilder(), block)
    }

    override fun build(): JDA =
        JDABuilder(token)
            .apply {
                setEnabledCacheFlags(cacheFlags)
                setEventManager(eventManager)
                setActivity(activity)
                setStatus(status)
                setBulkDeleteSplittingEnabled(bulkDeleteSplitting)
                setIdle(idle)
            }
            .also { builder ->
                commandClient?.let {
                    builder.addEventListeners(it)
                }
            }
            .build()
            .awaitReady()

    class CommandClientBuilder : Builder<CommandClient>() {
        var owner: Long = 0L
        lateinit var prefix: String
        var languages: List<Locale> = listOf(Locale.ENGLISH)
        var requiresEmbedLinks: Boolean = true
        var executor: CoroutineContext =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()).asCoroutineDispatcher()
        var guildSettingsProvider: GuildSettingsProvider? = null
        var uncaughtExceptionHandler: UncaughtExceptionHandler = DefaultUncaughtExceptionHandler
        var replies: Replies = DefaultReplies
        var colors: Colors = DefaultColors
        var emotes: Emotes = DefaultEmotes

        inline fun replies(block: Replies.() -> Unit) {
            replies = replies.apply(block)
        }

        inline fun colors(block: Colors.() -> Unit) {
            colors = colors.apply(block)
        }

        inline fun emotes(block: Emotes.() -> Unit) {
            emotes = emotes.apply(block)
        }

        override fun build(): CommandClient =
            CommandClient(
                owner,
                prefix,
                languages,
                requiresEmbedLinks,
                executor,
                guildSettingsProvider,
                uncaughtExceptionHandler,
                replies,
                colors,
                emotes
            )
    }
}
