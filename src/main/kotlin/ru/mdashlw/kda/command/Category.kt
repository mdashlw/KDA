package ru.mdashlw.kda.command

import net.dv8tion.jda.api.Permission
import java.util.*

abstract class Category {
    abstract val name: String
    open val memberPermissions: EnumSet<Permission> = EnumSet.noneOf(Permission::class.java)
    open val selfPermissions: EnumSet<Permission> = EnumSet.noneOf(Permission::class.java)
    open val displayInHelp: Boolean = true
    open val access: Command.Context.() -> Boolean = { true }
}
