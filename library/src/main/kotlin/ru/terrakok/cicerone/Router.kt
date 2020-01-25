package ru.terrakok.cicerone

import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.BackTo

interface Router {
    /**
     * Open new screen and add it to the screens chain.
     *
     * @param screen screen
     */
    fun navigateTo(screen: Screen)

    /**
     * Clear all screens and open new one as root.
     *
     * @param screen screen
     */
    fun newRootScreen(screen: Screen)

    /**
     * Replace current screen.
     * By replacing the screen, you alters the backstack,
     * so by going fragmentBack you will return to the previous screen
     * and not to the replaced one.
     *
     * @param screen screen
     */
    fun replaceScreen(screen: Screen)

    /**
     * Return fragmentBack to the needed screen from the chain.
     * Behavior in the case when no needed screens found depends on
     * the processing of the [BackTo] command in a [Navigator] implementation.
     *
     * @param screen screen
     */
    fun backTo(screen: Screen)

    /**
     * Opens several screens inside single transaction.
     * @param screens
     */
    fun newChain(vararg screens: Screen)

    /**
     * Clear current stack and open several screens inside single transaction.
     * @param screens
     */
    fun newRootChain(vararg screens: Screen)

    /**
     * Remove all screens from the chain and exit.
     * It's mostly used to finish the application or close a supplementary navigation chain.
     */
    fun finishChain()

    /**
     * Return to the previous screen in the chain.
     * Behavior in the case when the current screen is the root depends on
     * the processing of the [Back] command in a [Navigator] implementation.
     */
    fun exit()
}