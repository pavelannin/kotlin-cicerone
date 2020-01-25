package ru.terrakok.cicerone.android

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.Screen

/**
 * AppScreen is base interface for description and creation application screen.
 * NOTE: If you have described the creation of Intent then Activity will be started.
 * Recommendation: Use Intents for launch external application.
 *
 * Support @JvmName on interface.
 * https://youtrack.jetbrains.com/issue/KT-31420
 */
@Suppress(names = ["INAPPLICABLE_JVM_NAME"])
interface AppScreen : Screen {

    @get:JvmName(name = "getCreatorType") val creatorType: CreatorType

    interface CreatorType

    sealed class BaseCreatorType : CreatorType {
        data class ActivityCreator(val creator: (Context) -> Intent) : BaseCreatorType()
        data class FragmentCreator(val creator: () -> Fragment) : BaseCreatorType()
    }
}
