package ru.mdashlw.kda.api.command

abstract class OwnerCommand : PrivateCommand() {
    override val users: Set<Long> = emptySet()
}
