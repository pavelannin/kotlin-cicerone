/*
 * Created by Konstantin Tskhovrebov (aka @terrakok)
 */
package ru.terrakok.cicerone

import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace

/**
 * Router is the class for high-level navigation.
 * Use it to perform needed transitions.<br></br>
 * This implementation covers almost all cases needed for the average app.
 * Extend it if you need some tricky navigation.
 */
class AppRouter(private val executor: CommandExecutable) : Router {

    /**
     * Open new screen and add it to the screens chain.
     *
     * @param screen screen
     */
    override fun navigateTo(screen: Screen) {
        executor.execute(Forward(screen))
    }

    /**
     * Clear all screens and open new one as root.
     *
     * @param screen screen
     */
    override fun newRootScreen(screen: Screen) {
        executor.execute(BackTo(screen = null), Replace(screen))
    }

    /**
     * Replace current screen.
     * By replacing the screen, you alters the backstack,
     * so by going fragmentBack you will return to the previous screen
     * and not to the replaced one.
     *
     * @param screen screen
     */
    override fun replaceScreen(screen: Screen) {
        executor.execute(Replace(screen))
    }

    /**
     * Return fragmentBack to the needed screen from the chain.
     * Behavior in the case when no needed screens found depends on
     * the processing of the [BackTo] command in a [Navigator] implementation.
     *
     * @param screen screen
     */
    override fun backTo(screen: Screen) {
        executor.execute(BackTo(screen))
    }

    /**
     * Opens several screens inside single transaction.
     * @param screens
     */
    override fun newChain(vararg screens: Screen) {
        executor.execute(commands = screens.map { Forward(screen = it) }.toTypedArray())
    }

    /**
     * Clear current stack and open several screens inside single transaction.
     * @param screens
     */
    override fun newRootChain(vararg screens: Screen) {
        executor.execute(
            commands = arrayOf<Command>(BackTo(screen = null)) + screens.map { Forward(screen = it) }.toTypedArray()
        )
    }

    /**
     * Remove all screens from the chain and exit.
     * It's mostly used to finish the application or close a supplementary navigation chain.
     */
    override fun finishChain() {
        executor.execute(BackTo(screen = null), Back())
    }

    /**
     * Return to the previous screen in the chain.
     * Behavior in the case when the current screen is the root depends on
     * the processing of the [Back] command in a [Navigator] implementation.
     */
    override fun exit() {
        executor.execute(Back())
    }
}