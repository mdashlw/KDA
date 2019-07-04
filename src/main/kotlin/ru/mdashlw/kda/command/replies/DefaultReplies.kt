package ru.mdashlw.kda.command.replies

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.requests.restaction.MessageAction
import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.Replies
import ru.mdashlw.kda.command.manager.CommandManager
import java.time.OffsetDateTime

object DefaultReplies : Replies {
    override var help: (command: Command, context: Command.Context) -> MessageAction =
        { command: Command, context: Command.Context ->
            val aliases = command.aliases
            val examples = command.examples
            val memberPermissions = command.memberPermissions
            val selfPermissions = command.selfPermissions
            val prefix = context.guildSettings.prefix
            val selfUser = context.jda.selfUser

            context.reply {
                title = "Command ${command.name}"
                timestamp = OffsetDateTime.now()
                description = command.description

                footer {
                    text = "Prefix: $prefix"
                    icon = selfUser.effectiveAvatarUrl
                }

                field {
                    name = "Usage:"
                    value = "```\n$prefix${command.usage.trim()}\n```"
                    inline = false
                }

                if (examples.isNotEmpty()) {
                    field {
                        name = "Examples:"
                        value = "```\n${examples.joinToString("\n") { "$prefix${it.trim()}" }}\n```"
                        inline = false
                    }
                }

                field {
                    name = "Aliases:"
                    value = if (aliases.isEmpty()) {
                        "`None`"
                    } else {
                        aliases.joinToString { "`$it`" }
                    }
                    inline = false
                }

                if (!memberPermissions.isNullOrEmpty()) {
                    field {
                        name = "Required permissions:"
                        value = memberPermissions.joinToString(
                            "\n",
                            prefix = "**",
                            postfix = "**",
                            transform = Permission::getName
                        )
                    }
                }

                if (!selfPermissions.isNullOrEmpty()) {
                    field {
                        name = "Bot permissions:"
                        value = selfPermissions.joinToString(
                            "\n",
                            prefix = "**",
                            postfix = "**",
                            transform = Permission::getName
                        )
                    }
                }
            }
        }

    override var success: (command: Command, context: Command.Context, message: String) -> MessageAction =
        { _: Command, context: Command.Context, message: String ->
            context.reply {
                color = CommandManager.colors.success
                title = "Success"
                description = "${CommandManager.emotes.success.orEmpty()} $message"
            }
        }

    override var error: (command: Command, context: Command.Context, message: String) -> MessageAction =
        { _: Command, context: Command.Context, message: String ->
            context.reply {
                color = CommandManager.colors.error
                title = "Error"
                description = "${CommandManager.emotes.error.orEmpty()} $message"
            }
        }
}
