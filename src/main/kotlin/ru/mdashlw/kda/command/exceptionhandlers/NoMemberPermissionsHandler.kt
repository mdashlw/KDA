package ru.mdashlw.kda.command.exceptionhandlers

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.ExceptionHandler
import ru.mdashlw.kda.command.exceptions.NoMemberPermissionsException

object NoMemberPermissionsHandler :
    ExceptionHandler<NoMemberPermissionsException>(NoMemberPermissionsException::class) {
    override fun handle(command: Command, context: Command.Context, exception: NoMemberPermissionsException) {
        context.replyError("You need the following permissions to use this command: ${command.memberPermissions!!.joinToString { "**${it.getName()}**" }}.")
            .queue()
    }
}
