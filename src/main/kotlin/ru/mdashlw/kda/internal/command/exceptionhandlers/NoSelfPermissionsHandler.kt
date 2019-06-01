package ru.mdashlw.kda.internal.command.exceptionhandlers

import net.dv8tion.jda.api.Permission
import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.ExceptionHandler
import ru.mdashlw.kda.api.command.exceptions.NoSelfPermissionsException

object NoSelfPermissionsHandler : ExceptionHandler<NoSelfPermissionsException> {
    override fun handle(command: Command, event: Command.Event, exception: NoSelfPermissionsException) {
        event.replyError(
            event.localize(
                "exceptionhandlers.no_self_permissions",
                command.selfPermissions!!.joinToString("\n", "**", "**", transform = Permission::getName)
            )
        ).queue()
    }
}
