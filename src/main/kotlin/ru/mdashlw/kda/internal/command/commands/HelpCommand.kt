package ru.mdashlw.kda.internal.command.commands

import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.annotations.CommandFunction
import ru.mdashlw.kda.internal.command.CommandClient

object HelpCommand : Command() {
    override val name: String
        get() = "help"
    override val aliases: List<String>
        get() = listOf("commands", "cmds")
    override var usage: String = "[command]"

    override fun description(event: Event): String = event.localize("commands.help.description")

    @CommandFunction
    fun Event.help() {
        val commands = CommandClient.INSTANCE.commands.values
            .distinct()
            .filter(Command::displayInHelp)

        val selfUser = jda.selfUser

        reply {
            description = localize("commands.help.reply.description", prefix)

            author {
                name = localize("commands.help.reply.title", selfUser.name)
                icon = selfUser.effectiveAvatarUrl
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
