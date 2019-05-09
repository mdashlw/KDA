package ru.mdashlw.kda.command.exceptionhandler.impl

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.exceptionhandler.ExceptionHandler

object CommandHelpHandler : ExceptionHandler<Command.Help> {
    override fun handle(command: Command, event: Command.Event, exception: Command.Help) {
        event.replyHelp().queue()
    }
}
