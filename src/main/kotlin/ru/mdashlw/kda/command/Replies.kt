package ru.mdashlw.kda.command

import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.requests.restaction.MessageAction

interface Replies {
    var help: (command: Command, context: Command.Context) -> MessageAction

    var success: (command: Command, context: Command.Context, message: String) -> MessageAction

    var error: (command: Command, context: Command.Context, message: String) -> MessageAction

    var wrongChannel: (command: Command, context: Command.Context, channel: TextChannel) -> MessageAction
}
