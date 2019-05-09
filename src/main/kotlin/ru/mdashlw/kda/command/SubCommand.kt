package ru.mdashlw.kda.command

abstract class SubCommand(val parent: Command) : Command() {
    override fun register() {
        fixMeta()

        parent.subCommands[name.toLowerCase()] = this
        aliases?.forEach { parent.subCommands[it.toLowerCase()] = this }
    }
}
