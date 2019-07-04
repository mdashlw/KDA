package ru.mdashlw.kda.command.contexts

import net.dv8tion.jda.api.entities.User
import ru.mdashlw.kda.command.Command
import java.util.regex.Matcher

fun Command.Context.user(fallback: User? = null): User {
    val arg = take() ?: return fallback ?: throw Command.Help()

    return message.mentionedUsers.elementAtOrNull(index)
        ?: jda.getUsersByName(arg, true).firstOrNull()
        ?: arg.let {
            val matcher = User.USER_TAG.matcher(it).takeIf(Matcher::matches) ?: return@let null

            jda.getUserByTag(matcher.group(1), matcher.group(2))
        }
        ?: arg.toLongOrNull()?.let { jda.getUserById(it) }
        ?: error("User `$arg` does not exist.")
}
