package ru.mdashlw.kda.command.commands

import ru.mdashlw.kda.command.Command
import ru.mdashlw.kda.command.annotations.CommandFunction
import ru.mdashlw.kda.command.client.CommandClient

object HelpCommand : Command() {
    override val name: String = "help"
    override val aliases: List<String>? = listOf("commands", "cmds")
    override var usage: String = "[command]"

    override fun description(event: Event): String = event.localize("commands.help.description")

    @CommandFunction
    fun Event.help() {
        val commands = CommandClient.INSTANCE.commands.values
            .distinct()
            .filter(Command::displayInHelp)

        val self = jda.selfUser

        reply {
            description = localize("commands.help.reply.description", prefix)

            author {
                name = localize("commands.help.reply.title", self.name)
                icon = self.effectiveAvatarUrl
            }

            field {
                name = localize("commands.help.reply.fields.command.name")
                value = commands.joinToString("\n", "**", "**", transform = Command::name)
            }

            field {
                name = localize("commands.help.reply.fields.description.name")
                value = commands.joinToString("\n") { it.description(this@help).substringBefore("\n") }
            }
        }.queue()
    }

    @CommandFunction
    fun Event.help(command: Command) {
        copy(command).replyHelp().queue()
    }
}
