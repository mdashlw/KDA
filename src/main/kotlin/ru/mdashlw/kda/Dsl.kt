package ru.mdashlw.kda

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import ru.mdashlw.kda.builder.Builder
import ru.mdashlw.kda.builder.impl.EmbedBuilder
import ru.mdashlw.kda.builder.impl.JdaBuilder
import ru.mdashlw.kda.builder.impl.PaginationBuilder
import ru.mdashlw.kda.command.Colors
import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.Emotes
import ru.mdashlw.kda.command.Replies
import ru.mdashlw.kda.command.manager.CommandManager
import ru.mdashlw.kda.pagination.Pagination

inline fun <B : Builder<T>, T> build(builder: B, block: B.() -> Unit): T =
    builder.apply(block).build()

inline fun jda(block: JdaBuilder.() -> Unit): JDA = build(JdaBuilder(), block)

inline fun embed(parent: MessageEmbed? = null, block: EmbedBuilder.() -> Unit): MessageEmbed =
    build(EmbedBuilder(parent), block)

inline fun <T> pagination(content: Collection<T>, block: PaginationBuilder<T>.() -> Unit): Pagination<T> =
    build(PaginationBuilder(content), block)

fun command(block: Command.() -> Unit) {
    Command().apply(block).run {
        CommandManager.commands[name] = this
        aliases.forEach { CommandManager.commands[it] = this }
        hiddenAliases.forEach { CommandManager.commands[it] = this }
    }
}

fun Command.command(block: Command.() -> Unit) {
    Command(this).apply(block).run {
        this@command.commands[name] = this
        aliases.forEach { this@command.commands[it] = this }
        hiddenAliases.forEach { this@command.commands[it] = this }
    }
}

fun Command.action(minArgs: Int = 0, maxArgs: Int = 0, typing: Boolean = false, block: Command.Context.() -> Unit) {
    actions += Command.Action(minArgs, maxArgs, typing, block)
}

fun Command.access(block: Command.Context.() -> Boolean) {
    access = block
}

inline fun CommandManager.replies(block: Replies.() -> Unit) {
    replies = replies.apply(block)
}

inline fun CommandManager.colors(block: Colors.() -> Unit) {
    colors = colors.apply(block)
}

inline fun CommandManager.emotes(block: Emotes.() -> Unit) {
    emotes = emotes.apply(block)
}
