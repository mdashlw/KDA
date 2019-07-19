package ru.mdashlw.kda.command.exceptionhandlers

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.UncaughtExceptionHandler
import ru.mdashlw.kda.command.manager.CommandManager

object DefaultUncaughtExceptionHandler : UncaughtExceptionHandler() {
    override fun handle(command: Command, context: Command.Context, exception: Throwable) {
        exception.printStackTrace()

        val owner = CommandManager.owner.takeIf { it != 0L }?.let { context.jda.getUserById(it) }

        context.reply {
            color = CommandManager.colors.error
            title = "Unexpected Error"
            description = exception.toString()

            if (owner == null) {
                footer {
                    text = "Redirect this message to the developer"
                }
            } else {
                footer {
                    text = "Redirect this message to the developer: ${owner.asTag}"
                    icon = owner.effectiveAvatarUrl
                }
            }
        }.queue()
    }
}
