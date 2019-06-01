package ru.mdashlw.kda.api.builders

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.hooks.IEventManager
import net.dv8tion.jda.api.utils.cache.CacheFlag
import ru.mdashlw.kda.api.Builder
import ru.mdashlw.kda.api.build
import ru.mdashlw.kda.internal.command.CommandClient
import java.util.*

class JdaBuilder : Builder<JDA>() {
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
}
