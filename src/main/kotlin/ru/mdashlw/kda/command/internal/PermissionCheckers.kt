@file:Suppress("NOTHING_TO_INLINE")

package ru.mdashlw.kda.command.internal

import net.dv8tion.jda.api.entities.Member
import ru.mdashlw.kda.command.Command

internal inline fun Command.checkMemberPermissions(member: Member): Boolean =
    memberPermissions?.let(member::hasPermission) ?: true

internal inline fun Command.checkSelfPermissions(member: Member): Boolean =
    selfPermissions?.let(member::hasPermission) ?: true
