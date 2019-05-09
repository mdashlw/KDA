package ru.mdashlw.kda.command.replies.impl

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.requests.restaction.MessageAction
import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.client.CommandClient
import ru.mdashlw.kda.command.replies.Replies

object DefaultReplies : Replies {
    @Suppress("NestedLambdaShadowedImplicitParameter")
    override var help: (command: Command, event: Command.Event) -> MessageAction =
        { command, event ->
            val self = event.api.selfUser

            event.reply {
                title = "`${command.name}`"
                description = command.description

                thumbnail {
                    url = self.effectiveAvatarUrl
                }

                footer {
                    text = "Do not include <> or [] — They indicate <required> and [optional] arguments."
                }

                command.aliases?.let {
                    field {
                        name = "Aliases"
                        value = it.joinToString()
                        inline = false
                    }
                }

                field {
                    inline = false
                }

                field {
                    name = "Usage"
                    value = "`${event.guildSettings.prefix}${command.usage}`"
                    inline = false
                }

                command.examples?.let {
                    field {
                        name = "Examples"
                        value = it.joinToString("\n") { "${event.guildSettings.prefix}$it" }
                        inline = false
                    }
                }

                if (command.memberPermissions != null || command.selfPermissions != null) {
                    field {
                        inline = false
                    }

                    command.memberPermissions?.let {
                        field {
                            name = "Required Permissions"
                            value = it.joinToString("\n", "**", "**", transform = Permission::getName)
                        }
                    }

                    command.selfPermissions?.let {
                        field {
                            name = "Bot Permissions"
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
                title = "Success"
                description = "${CommandClient.INSTANCE.emotes.success} $message"
            }
        }

    override var warning: (command: Command, event: Command.Event, message: String) -> MessageAction =
        { _, event, message ->
            event.reply {
                color = CommandClient.INSTANCE.colors.warning
                title = "Warning"
                description = "${CommandClient.INSTANCE.emotes.warning} $message"
            }
        }

    override var error: (command: Command, event: Command.Event, message: String) -> MessageAction =
        { _, event, message ->
            event.reply {
                color = CommandClient.INSTANCE.colors.error
                title = "Error"
                description = "${CommandClient.INSTANCE.emotes.error} $message"
            }
        }
}
