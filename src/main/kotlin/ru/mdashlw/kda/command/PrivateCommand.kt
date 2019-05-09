package ru.mdashlw.kda.command

import ru.mdashlw.kda.command.client.CommandClient

abstract class PrivateCommand : Command() {
    abstract val users: Set<Long>

    override fun checkAccess(event: Event): Boolean =
        (event.member.idLong.run { this == CommandClient.INSTANCE.owner || this in users })
}
