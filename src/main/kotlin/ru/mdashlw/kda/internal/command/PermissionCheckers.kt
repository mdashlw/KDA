@file:Suppress("NOTHING_TO_INLINE")

package ru.mdashlw.kda.internal.command

import net.dv8tion.jda.api.entities.Member
import ru.mdashlw.kda.api.command.Command

internal inline fun Command.checkMemberPermissions(member: Member): Boolean =
    memberPermissions?.let(member::hasPermission) ?: true

internal inline fun Command.checkSelfPermissions(selfMember: Member): Boolean =
    selfPermissions?.let(selfMember::hasPermission) ?: true
