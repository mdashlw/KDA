package ru.mdashlw.kda.api.command

import ru.mdashlw.kda.internal.command.CommandClient

abstract class PrivateCommand : Command() {
    abstract val users: Set<Long>

    override fun checkAccess(event: Event): Boolean {
        val id = event.member.idLong

        return id == CommandClient.INSTANCE.ownerId || id in users
    }
}
