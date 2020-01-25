package ru.terrakok.cicerone.android

import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace

/**
 * Navigator interface for processing basic commands.
 */
interface AppNavigator : Navigator {

    /**
     * Perform transition described by the navigation command
     *
     * @param command the navigation command to apply
     */
    fun apply(command: Command)

    fun forward(command: Forward)

    fun back()

    fun replace(command: Replace)

    /**
     * Performs [BackTo] command transition
     */
    fun backTo(command: BackTo)

}