package ru.mdashlw.kda.command

abstract class OwnerCommand : PrivateCommand() {
    override val users: Set<Long> = emptySet()
}
