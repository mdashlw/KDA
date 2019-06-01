package ru.mdashlw.kda.api.command

import ru.mdashlw.kda.internal.command.CommandClient

abstract class PrivateCommand : Command() {
    abstract val users: Set<Long>

    override fun checkAccess(event: Event): Boolean =
        (event.member.idLong.run { this == CommandClient.INSTANCE.owner || this in users })
}
