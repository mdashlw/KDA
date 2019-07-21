package ru.mdashlw.kda.command.contexts

import ru.mdashlw.kda.command.Command

fun Command.Context.optionalText(limit: Int = args.size): String? = take(limit)

fun Command.Context.text(limit: Int = args.size, fallback: String? = null): String =
    optionalText(limit) ?: fallback ?: throw Command.Help()

fun Command.Context.optionalWord(): String? = optionalText(1)

fun Command.Context.word(fallback: String? = null): String = text(1, fallback)
