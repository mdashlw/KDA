package ru.mdashlw.kda.command.contexts

import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.User
import ru.mdashlw.kda.command.Command
import java.util.regex.Matcher

fun Command.Context.nullableMember(): Member? {
    val arg = take() ?: return null

    return message.mentionedMembers.elementAtOrNull(index)
        ?: guild.getMembersByName(arg, true).firstOrNull()
        ?: guild.getMembersByNickname(arg, true).firstOrNull()
        ?: arg.let {
            val matcher = User.USER_TAG.matcher(it).takeIf(Matcher::matches) ?: return@let null

            guild.getMemberByTag(matcher.group(1), matcher.group(2))
        }
        ?: arg.toLongOrNull()?.let { guild.getMemberById(it) }
        ?: error("Member `$arg` does not exist.")
}

fun Command.Context.member(fallback: Member? = null): Member = nullableMember() ?: fallback ?: throw Command.Help()
