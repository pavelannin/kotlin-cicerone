/*
 * Created by Konstantin Tskhovrebov (aka @terrakok)
 */
package ru.terrakok.cicerone

import ru.terrakok.cicerone.commands.Command

/**
 * The low-level navigation interface.
 * Navigator is the one who actually performs any transition.
 *
 * Support @JvmName on interface.
 * https://youtrack.jetbrains.com/issue/KT-31420
 */
@Suppress(names = ["INAPPLICABLE_JVM_NAME"])
interface Navigator {

    /**
     * Performs transition described by the navigation command.
     *
     * @param commands the navigation command array to apply per single transaction.
     */
    @JvmName(name = "applyCommands")
    fun apply(commands: Array<out Command>)
}
