package ru.mdashlw.kda.api.command

import net.dv8tion.jda.api.requests.restaction.MessageAction

interface Replies {
    var help: (command: Command, event: Command.Event) -> MessageAction

    var success: (command: Command, event: Command.Event, message: String) -> MessageAction

    var warning: (command: Command, event: Command.Event, message: String) -> MessageAction

    var error: (command: Command, event: Command.Event, message: String) -> MessageAction
}
