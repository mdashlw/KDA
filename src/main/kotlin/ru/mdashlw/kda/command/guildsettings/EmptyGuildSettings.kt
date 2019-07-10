package ru.mdashlw.kda.command.guildsettings

import ru.mdashlw.kda.command.GuildSettings
import ru.mdashlw.kda.command.manager.CommandManager

object EmptyGuildSettings : GuildSettings {
    override val prefix: String
        get() = CommandManager.prefix
    override val channel: Long
        get() = -1
}
