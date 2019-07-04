package ru.mdashlw.kda.builder.impl

import com.neovisionaries.ws.client.WebSocketFactory
import net.dv8tion.jda.api.AccountType
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.audio.factory.IAudioSendFactory
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.hooks.IEventManager
import net.dv8tion.jda.api.hooks.VoiceDispatchInterceptor
import net.dv8tion.jda.api.utils.Compression
import net.dv8tion.jda.api.utils.SessionController
import net.dv8tion.jda.api.utils.cache.CacheFlag
import net.dv8tion.jda.internal.JDAImpl
import net.dv8tion.jda.internal.managers.PresenceImpl
import net.dv8tion.jda.internal.utils.config.AuthorizationConfig
import net.dv8tion.jda.internal.utils.config.MetaConfig
import net.dv8tion.jda.internal.utils.config.SessionConfig
import net.dv8tion.jda.internal.utils.config.ThreadingConfig
import net.dv8tion.jda.internal.utils.config.flags.ConfigFlag
import okhttp3.OkHttpClient
import ru.mdashlw.kda.builder.Builder
import ru.mdashlw.kda.command.manager.CommandManager
import ru.mdashlw.kda.waiter.EventWaiter
import java.util.*
import java.util.concurrent.ConcurrentMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.ScheduledExecutorService

// ¯\_(ツ)_/¯
class JdaBuilder : Builder<JDA>() {
    val listeners = mutableListOf<Any>(EventWaiter)
    var accountType: AccountType = AccountType.BOT
    var rateLimitPool: ScheduledExecutorService? = null
    var shutdownRateLimitPool: Boolean = true
    var mainWsPool: ScheduledExecutorService? = null
    var shutdownMainWsPool: Boolean = true
    var callbackPool: ExecutorService? = null
    var shutdownCallbackPool: Boolean = true
    var cacheFlags: EnumSet<CacheFlag> = EnumSet.allOf(CacheFlag::class.java)
    var contextMap: ConcurrentMap<String, String>? = null
        set(value) {
            field = value
            if (value != null) {
                context = true
            }
        }
    var sessionController: SessionController? = null
    var voiceDispatchInterceptor: VoiceDispatchInterceptor? = null
    var httpClientBuilder: OkHttpClient.Builder? = null
    var httpClient: OkHttpClient? = null
    var websocketFactory: WebSocketFactory? = null
    lateinit var token: String
    var eventManager: IEventManager? = null
    var audioSendFactory: IAudioSendFactory? = null
    var activity: Activity? = null
    var status: OnlineStatus = OnlineStatus.ONLINE
        set(value) {
            if (value == OnlineStatus.UNKNOWN) {
                error("OnlineStatus cannot be unknown")
            }

            field = value
        }
    var idle: Boolean = false
    var maxReconnectDelay: Int = 900
        set(value) {
            if (value < 32) {
                error("Max reconnect delay must be 32 seconds or greater")
            }

            field = value
        }
    val flags: EnumSet<ConfigFlag> = ConfigFlag.getDefault()
    var compression: Compression = Compression.ZLIB
    var rawEvents: Boolean
        get() = ConfigFlag.RAW_EVENTS in flags
        set(value) = setFlag(ConfigFlag.RAW_EVENTS, value)
    var enabledCacheFlags: EnumSet<CacheFlag>?
        get() = cacheFlags
        set(value) {
            cacheFlags = value ?: EnumSet.noneOf(CacheFlag::class.java)
        }
    var disabledCacheFlags: EnumSet<CacheFlag>?
        get() = EnumSet.complementOf(cacheFlags)
        set(value) {
            cacheFlags = value?.let { EnumSet.complementOf(it) } ?: EnumSet.allOf(CacheFlag::class.java)
        }
    var context: Boolean
        get() = ConfigFlag.MDC_CONTEXT in flags
        set(value) = setFlag(ConfigFlag.MDC_CONTEXT, value)
    var requestTimeoutRetry: Boolean
        get() = ConfigFlag.RETRY_TIMEOUT in flags
        set(value) = setFlag(ConfigFlag.RETRY_TIMEOUT, value)
    var bulkDeleteSplitting: Boolean
        get() = ConfigFlag.BULK_DELETE_SPLIT in flags
        set(value) = setFlag(ConfigFlag.BULK_DELETE_SPLIT, value)
    var shutdownHook: Boolean
        get() = ConfigFlag.SHUTDOWN_HOOK in flags
        set(value) = setFlag(ConfigFlag.SHUTDOWN_HOOK, value)
    var autoReconnect: Boolean
        get() = ConfigFlag.AUTO_RECONNECT in flags
        set(value) = setFlag(ConfigFlag.AUTO_RECONNECT, value)

    fun setRateLimitPool(pool: ScheduledExecutorService?, automaticShutdown: Boolean = pool == null) {
        rateLimitPool = pool
        shutdownRateLimitPool = automaticShutdown
    }

    fun setGatewayPool(pool: ScheduledExecutorService?, automaticShutdown: Boolean = pool == null) {
        mainWsPool = pool
        shutdownMainWsPool = automaticShutdown
    }

    fun setCallbackPool(executor: ExecutorService?, automaticShutdown: Boolean = executor == null) {
        callbackPool = executor
        shutdownCallbackPool = automaticShutdown
    }

    fun addEventListeners(vararg listeners: Any) {
        this.listeners.addAll(listeners)
    }

    fun removeEventListeners(vararg listeners: Any) {
        this.listeners.removeAll(listeners)
    }

    fun setFlag(flag: ConfigFlag, state: Boolean) {
        if (state) {
            flags += flag
        } else {
            flags -= flag
        }
    }

    inline fun commandManager(block: CommandManager.() -> Unit) {
        listeners += CommandManager.apply(block)
    }

    override fun build(): JDA {
        val httpClient = httpClient ?: (httpClientBuilder ?: OkHttpClient.Builder()).build()
        val wsFactory = websocketFactory ?: WebSocketFactory()
        val authConfig = AuthorizationConfig(accountType, token)
        val threadingConfig = ThreadingConfig()

        threadingConfig.setCallbackPool(callbackPool, shutdownCallbackPool)
        threadingConfig.setGatewayPool(mainWsPool, shutdownMainWsPool)
        threadingConfig.setRateLimitPool(rateLimitPool, shutdownRateLimitPool)

        val sessionConfig =
            SessionConfig(sessionController, httpClient, wsFactory, voiceDispatchInterceptor, flags, maxReconnectDelay)
        val metaConfig = MetaConfig(contextMap, cacheFlags, flags)
        val jda = JDAImpl(authConfig, sessionConfig, threadingConfig, metaConfig)

        eventManager?.let(jda::setEventManager)
        audioSendFactory?.let(jda::setAudioSendFactory)
        listeners.forEach { jda.addEventListener(it) }
        jda.status = JDA.Status.INITIALIZED
        (jda.presence as PresenceImpl)
            .setCacheActivity(activity)
            .setCacheIdle(idle)
            .setCacheStatus(status)
        jda.login(null, compression, true)

        return jda
    }
}

