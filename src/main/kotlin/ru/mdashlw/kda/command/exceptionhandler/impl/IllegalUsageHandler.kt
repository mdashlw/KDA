package ru.mdashlw.kda.command.exceptionhandler.impl

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.client.CommandClient
import ru.mdashlw.kda.command.exceptionhandler.ExceptionHandler
import ru.mdashlw.kda.command.exceptions.IllegalUsageException

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
