package ru.mdashlw.kda.command.commands

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.annotations.CommandFunction
import ru.mdashlw.kda.command.client.CommandClient

object HelpCommand : Command() {
    override val name: String = "help"
    override val aliases: List<String>? = listOf("commands", "cmds")
    override val description: String = "Displays all commands of the bot"
    override var usage: String = "[command]"

    @CommandFunction
    fun Event.help() {
        val commands = CommandClient.INSTANCE.commands.values
            .distinct()
            .filter(Command::displayInHelp)

        val self = api.selfUser

        reply {
            description = localize("commands.help.description", prefix)

            author {
                name = localize("commands.help.title", self.name)
                icon = self.effectiveAvatarUrl
            }

            field {
                name = localize("commands.help.fields.command.title")
                value = commands.joinToString("\n", "**", "**", transform = Command::name)
            }

            field {
                name = localize("commands.help.fields.description.title")
                value = commands.joinToString("\n") { it.description.substringBefore("\n") }
            }
        }.queue()
    }

    @CommandFunction
    fun Event.help(command: Command) {
        copy(command).replyHelp().queue()
    }
}
