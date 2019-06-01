package ru.mdashlw.kda.internal.command

import ru.mdashlw.kda.api.command.Command
import ru.mdashlw.kda.api.command.Context
import ru.mdashlw.kda.api.command.exceptions.IllegalUsageException
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.ExecutionException
import kotlin.reflect.KFunction
import kotlin.reflect.full.extensionReceiverParameter
import kotlin.reflect.full.instanceParameter

internal object CommandExecutor {
    private fun execute(
        command: Command,
        event: Command.Event,
        function: KFunction<Unit>,
        args: List<String> = emptyList()
    ) {
        if (command.sendTyping) {
            event.channel.sendTyping().queue()
        }

        val arguments = try {
            CommandArgsParser.parse(event, function, args)
        } catch (exception: Context.Error) {
            throw IllegalUsageException(exception.message ?: "(no message)")
        }

        val baseArguments =
            mapOf(
                function.instanceParameter!! to command,
                function.extensionReceiverParameter!! to event
            )

        try {
            function.callBy(baseArguments + arguments)
        } catch (exception: InvocationTargetException) {
            throw exception.cause ?: exception
        } catch (exception: ExecutionException) {
            throw exception.cause ?: exception
        }
    }

    fun execute(command: Command, event: Command.Event, args: List<String> = emptyList()) {
        val function = command.findFunction(args.size)
            ?: command.findFunction(args.size + 1)
            ?: throw Command.Help()

        return execute(command, event, function, args)
    }
}
