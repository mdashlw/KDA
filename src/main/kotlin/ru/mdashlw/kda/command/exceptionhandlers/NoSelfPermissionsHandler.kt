package ru.mdashlw.kda.command.exceptionhandlers

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.ExceptionHandler
import ru.mdashlw.kda.command.exceptions.NoSelfPermissionsException

object NoSelfPermissionsHandler : ExceptionHandler<NoSelfPermissionsException>(NoSelfPermissionsException::class) {
    override fun handle(command: Command, context: Command.Context, exception: NoSelfPermissionsException) {
        context.replyError(
            "I need the following permissions to perform this command: " +
                    "${command.selfPermissions.joinToString { "**${it.getName()}**" }}."
        ).queue()
    }
}
