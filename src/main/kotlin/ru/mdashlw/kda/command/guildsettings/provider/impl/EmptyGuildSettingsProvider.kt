package ru.mdashlw.kda.command.guildsettings.provider.impl

import net.dv8tion.jda.api.entities.Guild
import ru.mdashlw.kda.command.guildsettings.GuildSettings
import ru.mdashlw.kda.command.guildsettings.impl.EmptyGuildSettings
import ru.mdashlw.kda.command.guildsettings.provider.GuildSettingsProvider

object EmptyGuildSettingsProvider : GuildSettingsProvider {
    override fun provide(guild: Guild): GuildSettings = EmptyGuildSettings
}
