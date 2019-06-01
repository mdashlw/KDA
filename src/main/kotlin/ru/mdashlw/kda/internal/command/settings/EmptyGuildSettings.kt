package ru.mdashlw.kda.internal.command.settings

import ru.mdashlw.kda.api.command.GuildSettings
import ru.mdashlw.kda.internal.command.CommandClient
import java.util.*

object EmptyGuildSettings : GuildSettings {
    override val prefix: String = CommandClient.INSTANCE.prefix
    override val locale: Locale = Locale.ENGLISH
}
