package ru.mdashlw.kda.api.command

import ru.mdashlw.kda.internal.command.CommandClient

abstract class PrivateSubCommand(parent: Command) : SubCommand(parent) {
    open val users: Set<Long> =
        ((parent as? PrivateCommand)?.users ?: (parent as? PrivateSubCommand)?.users).orEmpty()

    override fun checkAccess(event: Event): Boolean {
        val id = event.member.idLong

        return id == CommandClient.INSTANCE.ownerId || id in users
    }
}
