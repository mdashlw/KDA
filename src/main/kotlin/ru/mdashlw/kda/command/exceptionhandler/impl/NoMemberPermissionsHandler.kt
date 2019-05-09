package ru.mdashlw.kda.command.exceptionhandler.impl

import net.dv8tion.jda.api.Permission
import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.exceptionhandler.ExceptionHandler
import ru.mdashlw.kda.command.exceptions.NoMemberPermissionsException

object NoMemberPermissionsHandler : ExceptionHandler<NoMemberPermissionsException> {
    override fun handle(command: Command, event: Command.Event, exception: NoMemberPermissionsException) {
        event.replyError(
            "You need the following permissions to execute this command:\n" +
                    command.memberPermissions!!.joinToString("\n", "**", "**", transform = Permission::getName)
        ).queue()
    }
}
