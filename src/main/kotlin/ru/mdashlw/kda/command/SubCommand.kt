package ru.mdashlw.kda.command

abstract class SubCommand(val parent: Command) : Command() {
    override fun fixMeta() {
        val name = resolveNames(this).reversed().joinToString(" ")

        usage = "$name $usage".trim()
        examples = examples?.map { "$name $it".trim() }

        if (memberPermissions == null) {
            memberPermissions = parent.memberPermissions
        }

        if (selfPermissions == null) {
            selfPermissions = parent.selfPermissions
        }
    }

    private fun resolveNames(command: Command): List<String> {
        val names = mutableListOf(command.name)

        if (command is SubCommand) {
            names += resolveNames(command.parent)
        }

        return names
    }

    override fun register() {
        fixMeta()

        parent.subCommands[name.toLowerCase()] = this
        aliases?.forEach { parent.subCommands[it.toLowerCase()] = this }
    }
}
