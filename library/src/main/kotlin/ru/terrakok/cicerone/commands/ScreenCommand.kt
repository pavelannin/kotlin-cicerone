package ru.terrakok.cicerone.commands

import ru.terrakok.cicerone.Screen

/**
 * Navigation command describes screens transition.
 * that can be processed by [ru.terrakok.cicerone.Navigator].
 *
 * Support @JvmName on interface.
 * https://youtrack.jetbrains.com/issue/KT-31420
 */
@Suppress(names = ["INAPPLICABLE_JVM_NAME"])
interface ScreenCommand : Command {

    @get:JvmName(name = "getScreen") val screen: Screen
}
