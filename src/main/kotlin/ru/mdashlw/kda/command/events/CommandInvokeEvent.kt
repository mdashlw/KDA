package ru.mdashlw.kda.command.events

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.events.Event
import ru.mdashlw.kda.command.Command

class CommandInvokeEvent(
    api: JDA,
    responseNumber: Long,
    val command: Command,
    val event: Command.Event,
    val args: List<String>
) : Event(api, responseNumber)
