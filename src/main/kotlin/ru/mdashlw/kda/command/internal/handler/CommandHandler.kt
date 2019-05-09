package ru.mdashlw.kda.command.internal.handler

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.client.CommandClient
import ru.mdashlw.kda.command.exceptions.NoAccessException
import ru.mdashlw.kda.command.exceptions.NoMemberPermissionsException
import ru.mdashlw.kda.command.exceptions.NoSelfPermissionsException
import ru.mdashlw.kda.command.internal.checkMemberPermissions
import ru.mdashlw.kda.command.internal.checkSelfPermissions
import ru.mdashlw.kda.command.internal.executor.CommandExecutor
import ru.mdashlw.kda.command.internal.findCommand
import ru.mdashlw.kda.command.internal.findExceptionHandler

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
                return handle(subCommand, event.copy(subCommand), args.drop(1))
            }

            CommandExecutor.execute(command, event, args)
        } catch (exception: Throwable) {
            val handler = findExceptionHandler(exception.javaClass.kotlin)
                ?: CommandClient.INSTANCE.uncaughtExceptionHandler

            handler.handle(command, event, exception)
        }
    }
}
