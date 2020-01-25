package ru.terrakok.cicerone.android

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.commands.Back
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace
import java.util.LinkedList

/**
 * Navigator implementation for launch fragments and activities.
 * Feature [BackTo] works only for fragments.
 * Recommendation: most useful for Single-Activity application.
 */
class AppNavigatorImpl @JvmOverloads constructor(
    private val activity: FragmentActivity,
    private val fragmentManager: FragmentManager,
    private val containerId: Int,
    private val middleware: Middleware? = null
) : AppNavigator {

    private val localStack: LinkedList<String> by lazy { LinkedList(fragmentManager.copiedStack()) }

    override fun apply(commands: Array<out Command>) {
        fragmentManager.executePendingTransactions()

        localStack.clear()
        localStack.addAll(fragmentManager.copiedStack())

        commands.forEach { apply(command = it) }
    }

    /**
     * Perform transition described by the navigation command.
     *
     * @param command the navigation command to apply.
     */
    override fun apply(command: Command) {
        when (command) {
            is Forward -> forward(command)
            is Replace -> replace(command)
            is BackTo -> backTo(command)
            is Back -> back()
            else -> error("Navigator did not process the command: $command")
        }
    }

    override fun forward(command: Forward) {

        fun presentActivity(activityIntent: Intent) {
            if (activityIntent.isSafe(activity)) {
                activity.startActivity(activityIntent, middleware?.createActivityOptions(command, activityIntent))
            }
        }

        fun presentFragment(fragment: Fragment, tag: String) {
            val currentFragment by lazy { fragmentManager.findFragmentById(containerId) }
            (middleware?.setupFragmentTransaction(command, currentFragment, fragment, fragmentManager.beginTransaction())
                ?: fragmentManager.beginTransaction())
                .replace(containerId, fragment)
                .addToBackStack(tag)
                .commit()
            localStack.add(tag)
        }

        val screen = command.screen.asAppScreen()
        when (val creatorType = screen.creatorType.asBaseCreatorType()) {
            is AppScreen.BaseCreatorType.ActivityCreator -> presentActivity(creatorType.creator(activity))
            is AppScreen.BaseCreatorType.FragmentCreator -> presentFragment(creatorType.creator(), tag = screen.key)
        }
    }

    override fun back() {

        fun activityBack() {
            activity.finish()
        }

        fun fragmentBack() {
            fragmentManager.popBackStack()
            localStack.removeLast()
        }

        if (localStack.size > 0) fragmentBack() else activityBack()
    }

    override fun replace(command: Replace) {

        fun replaceActivity(activityIntent: Intent) {
            if (activityIntent.isSafe(activity)) {
                activity.startActivity(activityIntent, middleware?.createActivityOptions(command, activityIntent))
            }
            activity.finish()
        }

        fun replaceFragment(fragment: Fragment, tag: String) {
            val currentFragment by lazy { fragmentManager.findFragmentById(containerId) }

            if (localStack.size > 0) {
                fragmentManager.popBackStack()
                localStack.removeLast()

                (middleware?.setupFragmentTransaction(command, currentFragment, fragment, fragmentManager.beginTransaction())
                    ?: fragmentManager.beginTransaction())
                    .replace(containerId, fragment)
                    .addToBackStack(tag)
                    .commit()

                localStack.add(tag)
            } else {
                (middleware?.setupFragmentTransaction(command, currentFragment, fragment, fragmentManager.beginTransaction())
                    ?: fragmentManager.beginTransaction())
                    .replace(containerId, fragment)
                    .commit()
            }
        }

        val screen = command.screen.asAppScreen()
        when (val creatorType = screen.creatorType.asBaseCreatorType()) {
            is AppScreen.BaseCreatorType.ActivityCreator -> replaceActivity(creatorType.creator(activity))
            is AppScreen.BaseCreatorType.FragmentCreator -> replaceFragment(creatorType.creator(), tag = screen.key)
        }
    }

    /**
     * Performs [BackTo] command transition.
     */
    override fun backTo(command: BackTo) {

        fun backToRoot() {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            localStack.clear()
        }

        fun backToTag(tag: String) {
            localStack.indexOf(tag)
                .takeIf { it != -1 }
                ?.let { index -> (1 until localStack.size - index) }
                ?.forEach { _ -> localStack.removeLast() }
                ?.apply { fragmentManager.popBackStack(tag, 0) }
                ?: backToRoot()
        }

        command.screen?.let { screen -> backToTag(screen.key) } ?: backToRoot()
    }

    private fun FragmentManager.copiedStack(): List<String> {
        return (0 until backStackEntryCount)
            .mapNotNull { index -> getBackStackEntryAt(index).name }
            .toList()
    }

    private fun Screen.asAppScreen(): AppScreen {
        return checkNotNull(this as? AppScreen)
    }

    private fun AppScreen.CreatorType.asBaseCreatorType(): AppScreen.BaseCreatorType {
        return checkNotNull(this as? AppScreen.BaseCreatorType)
    }

    private fun Intent.isSafe(context: Context): Boolean {
        return context.packageManager.queryIntentActivities(this, PackageManager.MATCH_DEFAULT_ONLY).isNotEmpty()
    }

    interface Middleware {

        /**
         * This method to create option for start activity.
         *
         * @param command current navigation command. Will be only [Forward] or [Replace].
         * @param activityIntent activity intent.
         * @return transition options.
         */
        fun createActivityOptions(command: Command, activityIntent: Intent): Bundle?

        /**
         * This method to setup fragment transaction [FragmentTransaction].
         * For example: setCustomAnimations(...), addSharedElement(...) or setReorderingAllowed(...).
         *
         * @param command current navigation command. Will be only [Forward] or [Replace].
         * @param currentFragment current fragment in container.
         * (for [Replace] command it will be screen previous in new chain, NOT replaced screen).
         * @param nextFragment next screen fragment.
         * @param fragmentTransaction fragment transaction.
         */
        fun setupFragmentTransaction(
            command: Command,
            currentFragment: Fragment?,
            nextFragment: Fragment,
            fragmentTransaction: FragmentTransaction
        ): FragmentTransaction
    }
}
