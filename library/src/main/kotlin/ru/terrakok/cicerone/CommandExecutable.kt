@file:JvmMultifileClass

package ru.terrakok.cicerone

import ru.terrakok.cicerone.commands.Command

/**
 *
 *
 * Support @JvmName on interface.
 * https://youtrack.jetbrains.com/issue/KT-31420
 */
@Suppress(names = ["INAPPLICABLE_JVM_NAME"])
interface CommandExecutable {

    @JvmName(name = "executeCommands")
    fun execute(commands: Array<out Command>)
}

fun CommandExecutable.execute(vararg commands: Command) {
    execute(commands)
}
