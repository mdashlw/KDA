package ru.mdashlw.kda.command

import ru.mdashlw.kda.builder.impl.EmbedBuilder
import ru.mdashlw.kda.command.manager.CommandManager

abstract class ReplyModifier {
    fun register() {
        CommandManager.replyModifiers += this
    }

    open fun check(command: Command, content: Command.Context): Boolean = true

    abstract fun modify(command: Command, context: Command.Context): EmbedBuilder.() -> Unit
}
