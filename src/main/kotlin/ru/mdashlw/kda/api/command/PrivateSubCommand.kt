package ru.mdashlw.kda.api.command

import ru.mdashlw.kda.internal.command.CommandClient

abstract class PrivateSubCommand(parent: Command) : SubCommand(parent) {
    open val users: Set<Long> =
        ((parent as? PrivateCommand)?.users ?: (parent as? PrivateSubCommand)?.users).orEmpty()

    override fun checkAccess(event: Event): Boolean =
        (event.member.idLong.run { this == CommandClient.INSTANCE.owner || this in users })
}
