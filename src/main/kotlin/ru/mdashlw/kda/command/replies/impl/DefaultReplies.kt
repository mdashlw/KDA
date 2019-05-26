package ru.mdashlw.kda.command.replies.impl

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.requests.restaction.MessageAction
import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.client.CommandClient
import ru.mdashlw.kda.command.replies.Replies

object DefaultReplies : Replies {
    override var help: (command: Command, event: Command.Event) -> MessageAction =
        { command, event ->
            val self = event.jda.selfUser

            event.reply {
                title = "`${command.name}`"
                description = command.description(event)

                thumbnail {
                    url = self.effectiveAvatarUrl
                }

                footer {
                    text = event.localize("replies.help.footer")
                }

                command.aliases?.let {
                    field {
                        name = event.localize("replies.help.fields.aliases.name")
                        value = it.joinToString()
                        inline = false
                    }
                }

                field {
                    inline = false
                }

                field {
                    name = event.localize("replies.help.fields.usage.name")
                    value = "`${event.prefix}${command.usage}`"
                    inline = false
                }

                command.examples?.let {
                    field {
                        name = event.localize("replies.help.fields.examples.name")
                        value = it.joinToString("\n") { "${event.prefix}$it" }
                        inline = false
                    }
                }

                if (command.memberPermissions != null || command.selfPermissions != null) {
                    field {
                        inline = false
                    }

                    command.memberPermissions?.let {
                        field {
                            name = event.localize("replies.help.fields.member_permissions.name")
                            value = it.joinToString("\n", "**", "**", transform = Permission::getName)
                        }
                    }

                    command.selfPermissions?.let {
                        field {
                            name = event.localize("replies.help.fields.self_permissions.name")
                            value = it.joinToString("\n", "**", "**", transform = Permission::getName)
                        }
                    }
                }
            }
        }

    override var success: (command: Command, event: Command.Event, message: String) -> MessageAction =
        { _, event, message ->
            event.reply {
                color = CommandClient.INSTANCE.colors.success
                title = event.localize("replies.success.title")
                description = "${CommandClient.INSTANCE.emotes.success} $message"
            }
        }

    override var warning: (command: Command, event: Command.Event, message: String) -> MessageAction =
        { _, event, message ->
            event.reply {
                color = CommandClient.INSTANCE.colors.warning
                title = event.localize("replies.warning.title")
                description = "${CommandClient.INSTANCE.emotes.warning} $message"
            }
        }

    override var error: (command: Command, event: Command.Event, message: String) -> MessageAction =
        { _, event, message ->
            event.reply {
                color = CommandClient.INSTANCE.colors.error
                title = event.localize("replies.error.title")
                description = "${CommandClient.INSTANCE.emotes.error} $message"
            }
        }
}
