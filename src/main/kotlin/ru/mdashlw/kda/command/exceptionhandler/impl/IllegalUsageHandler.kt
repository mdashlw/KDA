package ru.mdashlw.kda.command.exceptionhandler.impl

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.client.CommandClient
import ru.mdashlw.kda.command.exceptionhandler.ExceptionHandler
import ru.mdashlw.kda.command.exceptions.IllegalUsageException

object IllegalUsageHandler : ExceptionHandler<IllegalUsageException> {
    override fun handle(command: Command, event: Command.Event, exception: IllegalUsageException) {
        event.reply {
            color = CommandClient.INSTANCE.colors.error
            title = "Error"
            description = exception.message.toString()

            footer {
                text = "${event.guildSettings.prefix}${command.usage}"
            }
        }.queue()
    }
}
