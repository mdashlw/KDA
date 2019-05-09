package ru.mdashlw.kda.command.guildsettings.impl

import ru.mdashlw.kda.command.client.CommandClient
import ru.mdashlw.kda.command.guildsettings.GuildSettings

object EmptyGuildSettings : GuildSettings {
    override val prefix: String = CommandClient.INSTANCE.prefix
}
