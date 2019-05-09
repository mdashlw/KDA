package ru.mdashlw.kda.command.replies

import net.dv8tion.jda.api.requests.restaction.MessageAction
import ru.mdashlw.kda.command.Command

interface Replies {
    var help: (command: Command, event: Command.Event) -> MessageAction

    var success: (command: Command, event: Command.Event, message: String) -> MessageAction

    var warning: (command: Command, event: Command.Event, message: String) -> MessageAction

    var error: (command: Command, event: Command.Event, message: String) -> MessageAction
}
