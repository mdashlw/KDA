package ru.mdashlw.kda.command.guildsettings.provider

import net.dv8tion.jda.api.entities.Guild
import ru.mdashlw.kda.command.guildsettings.GuildSettings

interface GuildSettingsProvider {
    fun provide(guild: Guild): GuildSettings
}
