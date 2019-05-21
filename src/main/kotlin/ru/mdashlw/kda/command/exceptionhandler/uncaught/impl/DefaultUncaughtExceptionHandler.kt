package ru.mdashlw.kda.command.exceptionhandler.uncaught.impl

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.client.CommandClient
import ru.mdashlw.kda.command.exceptionhandler.uncaught.UncaughtExceptionHandler

object DefaultUncaughtExceptionHandler : UncaughtExceptionHandler {
    override fun handle(command: Command, event: Command.Event, exception: Throwable) {
        exception.printStackTrace()

        event.reply {
            color = CommandClient.INSTANCE.colors.error
            title = event.localize("exceptionhandlers.uncaught.title")
            description = exception.toString()

            event.api.getUserById(CommandClient.INSTANCE.owner)?.let {
                footer {
                    text = event.localize("exceptionhandlers.uncaught.footer", it.asTag)
                    icon = it.effectiveAvatarUrl
                }
            }
        }.queue()
    }
}
