package ru.mdashlw.kda.internal.command

import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.exceptions.NoAccessException
import ru.mdashlw.kda.api.command.exceptions.NoMemberPermissionsException
import ru.mdashlw.kda.api.command.exceptions.NoSelfPermissionsException

internal object CommandHandler {
    fun handle(command: Command, event: Command.Event, args: List<String>) {
        try {
            if (!command.checkAccess(event)) {
                throw NoAccessException()
            }

            if (!command.checkMemberPermissions(event.member)) {
                throw NoMemberPermissionsException()
            }

            if (!command.checkSelfPermissions(event.guild.selfMember)) {
                throw NoSelfPermissionsException()
            }

            if (args.isEmpty()) {
                CommandExecutor.execute(command, event)
                return
            }

            val subCommand = findCommand(args[0], command)

            if (subCommand != null) {
                return handle(
                    subCommand,
                    event.copy(subCommand),
                    args.drop(1)
                )
            }

            CommandExecutor.execute(command, event, args)
        } catch (exception: Throwable) {
            val handler = findExceptionHandler(exception.javaClass.kotlin)
                ?: CommandClient.INSTANCE.uncaughtExceptionHandler

            handler.handle(command, event, exception)
        }
    }
}
