package ru.mdashlw.kda.api.command

import net.dv8tion.jda.api.entities.Guild

interface GuildSettingsProvider {
    fun provide(guild: Guild): GuildSettings?
}
