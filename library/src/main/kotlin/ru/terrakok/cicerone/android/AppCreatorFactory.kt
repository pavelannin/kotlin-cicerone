@file:JvmMultifileClass

package ru.terrakok.cicerone.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ru.terrakok.cicerone.Screen
import ru.terrakok.cicerone.commands.Command

typealias FragmentTransactionModifier = FragmentTransaction.() -> (command: Command, current: Fragment?, next: Fragment) -> Unit

sealed class AppCreatorFactory : Screen.CreatorFactory

data class ActivityCreator @JvmOverloads constructor(
    val creator: (Context) -> Intent,
    val optionsProvider: ((Command) -> Bundle)? = null
) : AppCreatorFactory()

data class FragmentCreator @JvmOverloads constructor(
    val creator: () -> Fragment,
    val modifier: FragmentTransactionModifier? = null
) : AppCreatorFactory()
