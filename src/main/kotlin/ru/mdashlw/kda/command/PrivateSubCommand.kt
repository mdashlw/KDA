package ru.mdashlw.kda.command

import ru.mdashlw.kda.command.client.CommandClient

abstract class PrivateSubCommand(parent: Command) : SubCommand(parent) {
    open val users: Set<Long> = (parent as? PrivateCommand)?.users ?: emptySet()

    override fun checkAccess(event: Event): Boolean =
        (event.member.idLong.run { this == CommandClient.INSTANCE.owner || this in users })
}
