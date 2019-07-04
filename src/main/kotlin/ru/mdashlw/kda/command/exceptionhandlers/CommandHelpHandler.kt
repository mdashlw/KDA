package ru.mdashlw.kda.command.exceptionhandlers

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.ExceptionHandler

object CommandHelpHandler : ExceptionHandler<Command.Help>(Command.Help::class) {
    override fun handle(command: Command, context: Command.Context, exception: Command.Help) {
        context.replyHelp().queue()
    }
}
