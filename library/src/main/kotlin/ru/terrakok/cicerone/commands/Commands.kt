@file:JvmMultifileClass

package ru.terrakok.cicerone.commands

import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.Screen

/**
 * Rolls fragmentBack the last transition from the screens chain.
 */
class Back : Command

/**
 * Rolls fragmentBack to the needed screen from the screens chain.
 * Behavior in the case when no needed screens found depends on an implementation of the [Navigator].
 * But the recommended behavior is to return to the root.
 */
class BackTo(val screen: Screen?) : Command

/**
 * Opens new screen.
 */
class Forward(override val screen: Screen) : ScreenCommand

/**
 * Replaces the current screen.
 */
class Replace(override val screen: Screen) : ScreenCommand
