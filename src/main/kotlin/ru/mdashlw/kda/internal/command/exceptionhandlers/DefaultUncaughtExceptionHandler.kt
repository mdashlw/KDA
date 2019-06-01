package ru.mdashlw.kda.internal.command.exceptionhandlers

import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.UncaughtExceptionHandler
import ru.mdashlw.kda.internal.command.CommandClient

object DefaultUncaughtExceptionHandler : UncaughtExceptionHandler {
    override fun handle(command: Command, event: Command.Event, exception: Throwable) {
        exception.printStackTrace()

        event.reply {
            color = CommandClient.INSTANCE.colors.error
            title = event.localize("exceptionhandlers.uncaught.title")
            description = exception.toString()

            event.jda.getUserById(CommandClient.INSTANCE.ownerId)?.let {
                footer {
                    text = event.localize("exceptionhandlers.uncaught.footer", it.asTag)
                    icon = it.effectiveAvatarUrl
                }
            }
        }.queue()
    }
}
