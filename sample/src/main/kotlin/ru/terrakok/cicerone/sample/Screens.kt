package ru.terrakok.cicerone.sample

import android.content.Intent
import android.net.Uri
import ru.terrakok.cicerone.android.AppScreen
import ru.terrakok.cicerone.android.AppScreen.BaseCreatorType.ActivityCreator
import ru.terrakok.cicerone.android.AppScreen.BaseCreatorType.FragmentCreator
import ru.terrakok.cicerone.sample.ui.animations.ProfileActivity
import ru.terrakok.cicerone.sample.ui.animations.photos.SelectPhotoFragment
import ru.terrakok.cicerone.sample.ui.animations.profile.ProfileFragment
import ru.terrakok.cicerone.sample.ui.bottom.BottomNavigationActivity
import ru.terrakok.cicerone.sample.ui.bottom.ForwardFragment
import ru.terrakok.cicerone.sample.ui.bottom.TabContainerFragment
import ru.terrakok.cicerone.sample.ui.main.MainActivity
import ru.terrakok.cicerone.sample.ui.main.SampleFragment
import ru.terrakok.cicerone.sample.ui.start.StartActivity

sealed class Screens : AppScreen {

    data class SampleScreen(val number: Int) : Screens()
    object StartScreen : Screens()
    object MainScreen : Screens()
    object BottomNavigationScreen : Screens()
    data class TabScreen(val tabName: String) : Screens()
    data class ForwardScreen(val containerName: String, val number: Int) : Screens()
    data class GithubScreen(val url: Uri = Uri.parse("https://github.com/terrakok/Cicerone")) : Screens()
    object ProfileScreen : Screens()
    object ProfileInfoScreen : Screens()
    object SelectPhotoScreen : Screens()

    override val key: String get() = when (this) {
        is SampleScreen -> "${javaClass.simpleName}_$number"
        else -> javaClass.canonicalName ?: javaClass.simpleName
    }

    override val creatorType: AppScreen.CreatorType get() = when (this) {
        is SampleScreen -> FragmentCreator { SampleFragment.getNewInstance(number) }
        StartScreen -> ActivityCreator { Intent(it, StartActivity::class.java) }
        MainScreen -> ActivityCreator { Intent(it, MainActivity::class.java) }
        BottomNavigationScreen -> ActivityCreator { Intent(it, BottomNavigationActivity::class.java) }
        is TabScreen -> FragmentCreator { TabContainerFragment.getNewInstance(tabName) }
        is ForwardScreen -> FragmentCreator { ForwardFragment.getNewInstance(containerName, number) }
        is GithubScreen -> ActivityCreator { Intent(Intent.ACTION_VIEW, url) }
        ProfileScreen -> ActivityCreator { Intent(it, ProfileActivity::class.java) }
        ProfileInfoScreen -> FragmentCreator { ProfileFragment() }
        SelectPhotoScreen -> FragmentCreator { SelectPhotoFragment() }
    }
}
