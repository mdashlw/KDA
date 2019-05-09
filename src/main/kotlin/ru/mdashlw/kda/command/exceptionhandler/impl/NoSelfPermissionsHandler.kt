package ru.mdashlw.kda.command.exceptionhandler.impl

import net.dv8tion.jda.api.Permission
import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.exceptionhandler.ExceptionHandler
import ru.mdashlw.kda.command.exceptions.NoSelfPermissionsException

object NoSelfPermissionsHandler : ExceptionHandler<NoSelfPermissionsException> {
    override fun handle(command: Command, event: Command.Event, exception: NoSelfPermissionsException) {
        event.replyError(
            "I need the following permissions to perform this command:\n" +
                    command.selfPermissions!!.joinToString("\n", "**", "**", transform = Permission::getName)
        ).queue()
    }
}
