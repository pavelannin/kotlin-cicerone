package ru.terrakok.cicerone.android

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import ru.terrakok.cicerone.Navigator
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
open class AppNavigator(
    private val activity: FragmentActivity,
    private val fragmentManager: FragmentManager,
    private val containerId: Int
) : Navigator {

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
    protected open fun apply(command: Command) {
        when (command) {
            is Forward -> forward(command)
            is Replace -> replace(command)
            is BackTo -> backTo(command)
            is Back -> back()
            else -> error("Navigator did not process the command: $command")
        }
    }

    private fun forward(command: Forward) {

        fun presentActivity(activityIntent: Intent, options: Bundle?) {
            if (activityIntent.isSafe(activity)) {
                activity.startActivity(activityIntent, options)
            }
        }

        fun presentFragment(fragment: Fragment, tag: String, modifier: FragmentTransactionModifier?) {
            val currentFragment by lazy { fragmentManager.findFragmentById(containerId) }
            fragmentManager.beginTransaction()
                .also { transaction -> modifier?.invoke(transaction)?.invoke(command, currentFragment, fragment) }
                .replace(containerId, fragment)
                .addToBackStack(tag)
                .commit()
            localStack.add(tag)
        }

        val screen = command.screen
        when (val factory = screen.creatorFactory.asAppCreatorFactory()) {
            is ActivityCreator -> presentActivity(factory.creator(activity), factory.optionsProvider?.invoke(command))
            is FragmentCreator -> presentFragment(factory.creator(), tag = screen.key, modifier = factory.modifier)
        }
    }

    private fun back() {

        fun activityBack() {
            activity.finish()
        }

        fun fragmentBack() {
            fragmentManager.popBackStack()
            localStack.removeLast()
        }

        if (localStack.size > 0) fragmentBack() else activityBack()
    }

    private fun replace(command: Replace) {

        fun replaceActivity(activityIntent: Intent, options: Bundle?) {
            if (activityIntent.isSafe(activity)) {
                activity.startActivity(activityIntent, options)
            }
            activity.finish()
        }

        fun replaceFragment(fragment: Fragment, tag: String, modifier: FragmentTransactionModifier?) {
            val currentFragment by lazy { fragmentManager.findFragmentById(containerId) }

            if (localStack.size > 0) {
                fragmentManager.popBackStack()
                localStack.removeLast()

                fragmentManager.beginTransaction()
                    .also { transaction -> modifier?.invoke(transaction)?.invoke(command, currentFragment, fragment) }
                    .replace(containerId, fragment)
                    .addToBackStack(tag)
                    .commit()

                localStack.add(tag)
            } else {
                fragmentManager.beginTransaction()
                    .also { transaction -> modifier?.invoke(transaction)?.invoke(command, currentFragment, fragment) }
                    .replace(containerId, fragment)
                    .commit()
            }
        }

        val screen = command.screen
        when (val factory = screen.creatorFactory.asAppCreatorFactory()) {
            is ActivityCreator -> replaceActivity(factory.creator(activity), factory.optionsProvider?.invoke(command))
            is FragmentCreator -> replaceFragment(factory.creator(), tag = screen.key, modifier = factory.modifier)
        }
    }

    /**
     * Performs [BackTo] command transition.
     */
    private fun backTo(command: BackTo) {

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

    private fun Screen.CreatorFactory.asAppCreatorFactory(): AppCreatorFactory {
        return checkNotNull(this as? AppCreatorFactory)
    }

    private fun Intent.isSafe(context: Context): Boolean {
        return context.packageManager.queryIntentActivities(this, PackageManager.MATCH_DEFAULT_ONLY).isNotEmpty()
    }
}
