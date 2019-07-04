package ru.mdashlw.kda.command.contexts

import ru.mdashlw.kda.command.Command

fun Command.Context.word(fallback: String? = null): String = text(1, fallback)

fun Command.Context.text(limit: Int = args.size, fallback: String? = null): String =
    take(limit) ?: fallback ?: throw Command.Help()
