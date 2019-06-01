package ru.mdashlw.kda.internal.command.exceptionhandlers

import net.dv8tion.jda.api.Permission
import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.ExceptionHandler
import ru.mdashlw.kda.api.command.exceptions.NoMemberPermissionsException

object NoMemberPermissionsHandler : ExceptionHandler<NoMemberPermissionsException> {
    override fun handle(command: Command, event: Command.Event, exception: NoMemberPermissionsException) {
        event.replyError(
            event.localize(
                "exceptionhandlers.no_member_permissions",
                command.memberPermissions!!.joinToString("\n", "**", "**", transform = Permission::getName)
            )
        ).queue()
    }
}
