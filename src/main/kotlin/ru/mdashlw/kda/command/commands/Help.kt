package ru.mdashlw.kda.command.commands

import ru.mdashlw.kda.action
import ru.mdashlw.kda.command
import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.contexts.command
import ru.mdashlw.kda.command.manager.CommandManager

@Suppress("NestedLambdaShadowedImplicitParameter")
fun help() {
    command {
        name = "help"
        description = "Display all the commands"
        usage = "[command]"
        aliases = listOf("commands", "cmds")

        action {
            val commands = CommandManager.commands.values
                .distinct()
                .filter(Command::displayInHelp)
                .groupBy(Command::category)
                .toList()
            val selfUser = jda.selfUser

            reply {
                description = "**Use `${guildSettings.prefix}help <command>` for additional info.**"

                author {
                    name = "${selfUser.name} Commands"
                    icon = selfUser.effectiveAvatarUrl
                }

                commands.forEach { (category, list) ->
                    field {
                        name = "${category.name} Commands"
                        value = list.joinToString("\n") {
                            "**${it.name}** - ${it.description.substringBefore('\n')}"
                        }
                        inline = false
                    }
                }
            }.queue()
        }

        action(minArgs = 1, maxArgs = -1) {
            val command = command()

            copy(command).replyHelp().queue()
        }
    }
}
