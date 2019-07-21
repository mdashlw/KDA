package ru.mdashlw.kda.command.contexts

import ru.mdashlw.kda.command.Command

fun Command.Context.optionalWord(): String? = take()

fun Command.Context.word(fallback: String? = null): String = take() ?: fallback ?: throw Command.Help()

fun Command.Context.optionalText(limit: Int = args.size): String? = take(limit)

fun Command.Context.text(limit: Int = args.size, fallback: String? = null): String =
    take(limit) ?: fallback ?: throw Command.Help()
