package ru.terrakok.cicerone.sample.ui.main

import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import ru.terrakok.cicerone.android.AppNavigator
import ru.terrakok.cicerone.android.AppNavigatorImpl
import ru.terrakok.cicerone.commands.Command

class MainNavigator @JvmOverloads constructor(
    private val activity: FragmentActivity,
    private val containerId: Int,
    private val onApplyCommands: () -> Unit,
    private val fragmentManager: FragmentManager = activity.supportFragmentManager,
    private val navigator: AppNavigator = AppNavigatorImpl(activity, activity.supportFragmentManager, containerId)
) : AppNavigator by navigator {

    override fun apply(commands: Array<out Command>) {
        navigator.apply(commands)
        fragmentManager.executePendingTransactions()
        onApplyCommands()
    }
}
