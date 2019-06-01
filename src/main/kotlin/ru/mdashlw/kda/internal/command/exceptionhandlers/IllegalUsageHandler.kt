package ru.mdashlw.kda.internal.command.exceptionhandlers

import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.ExceptionHandler
import ru.mdashlw.kda.api.command.exceptions.IllegalUsageException
import ru.mdashlw.kda.internal.command.CommandClient

object IllegalUsageHandler : ExceptionHandler<IllegalUsageException> {
    override fun handle(command: Command, event: Command.Event, exception: IllegalUsageException) {
        event.reply {
            color = CommandClient.INSTANCE.colors.error
            title = event.localize("exceptionhandlers.illegal_usage.title")
            description = exception.message.toString()

            footer {
                text = "${event.prefix}${command.usage}"
            }
        }.queue()
    }
}
