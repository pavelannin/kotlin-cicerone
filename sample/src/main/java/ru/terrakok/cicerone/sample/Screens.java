package ru.terrakok.cicerone.sample;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import androidx.fragment.app.Fragment;

import ru.terrakok.cicerone.android.AppScreen;
import ru.terrakok.cicerone.sample.ui.animations.ProfileActivity;
import ru.terrakok.cicerone.sample.ui.animations.photos.SelectPhotoFragment;
import ru.terrakok.cicerone.sample.ui.animations.profile.ProfileFragment;
import ru.terrakok.cicerone.sample.ui.bottom.BottomNavigationActivity;
import ru.terrakok.cicerone.sample.ui.bottom.ForwardFragment;
import ru.terrakok.cicerone.sample.ui.bottom.TabContainerFragment;
import ru.terrakok.cicerone.sample.ui.main.MainActivity;
import ru.terrakok.cicerone.sample.ui.main.SampleFragment;
import ru.terrakok.cicerone.sample.ui.start.StartActivity;

/**
 * Created by Konstantin Tckhovrebov (aka @terrakok)
 * on 11.10.16
 */

public class Screens {
    public static final class SampleScreen implements AppScreen {
        private final int number;

        public SampleScreen(int number) {
            this.number = number;
        }

        @NotNull
        @Override
        public String getKey() {
            return getClass().getSimpleName() + "_" + number;
        }

        @Nullable
        @Override
        public Intent getActivityIntent(@NotNull Context context) {
            return null;
        }

        @Nullable
        @Override
        public Fragment getFragment() {
            return SampleFragment.getNewInstance(number);
        }
    }

    public static final class StartScreen implements AppScreen {

        @NotNull
        @Override
        public String getKey() {
            return getClass().getCanonicalName();
        }

        @Nullable
        @Override
        public Intent getActivityIntent(@NotNull Context context) {
            return new Intent(context, StartActivity.class);
        }

        @Nullable
        @Override
        public Fragment getFragment() {
            return null;
        }
    }

    public static final class MainScreen implements AppScreen {

        @NotNull
        @Override
        public String getKey() {
            return getClass().getCanonicalName();
        }

        @Nullable
        @Override
        public Intent getActivityIntent(@NotNull Context context) {
            return new Intent(context, MainActivity.class);
        }

        @Nullable
        @Override
        public Fragment getFragment() {
            return null;
        }
    }

    public static final class BottomNavigationScreen implements AppScreen {

        @NotNull
        @Override
        public String getKey() {
            return getClass().getCanonicalName();
        }

        @Nullable
        @Override
        public Intent getActivityIntent(@NotNull Context context) {
            return new Intent(context, BottomNavigationActivity.class);
        }

        @Nullable
        @Override
        public Fragment getFragment() {
            return null;
        }
    }

    public static final class TabScreen implements AppScreen {
        private final String tabName;

        public TabScreen(String tabName) {
            this.tabName = tabName;
        }

        @NotNull
        @Override
        public String getKey() {
            return getClass().getCanonicalName();
        }

        @Nullable
        @Override
        public Intent getActivityIntent(@NotNull Context context) {
            return null;
        }

        @Nullable
        @Override
        public Fragment getFragment() {
            return TabContainerFragment.getNewInstance(tabName);
        }
    }

    public static final class ForwardScreen implements AppScreen {

        private final String containerName;
        private final int number;

        public ForwardScreen(String containerName, int number) {
            this.containerName = containerName;
            this.number = number;
        }

        @NotNull
        @Override
        public String getKey() {
            return getClass().getCanonicalName();
        }

        @Nullable
        @Override
        public Intent getActivityIntent(@NotNull Context context) {
            return null;
        }

        @Nullable
        @Override
        public Fragment getFragment() {
            return ForwardFragment.getNewInstance(containerName, number);
        }
    }

    public static final class GithubScreen implements AppScreen {

        @NotNull
        @Override
        public String getKey() {
            return getClass().getCanonicalName();
        }

        @Nullable
        @Override
        public Intent getActivityIntent(@NotNull Context context) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/terrakok/Cicerone"));
        }

        @Nullable
        @Override
        public Fragment getFragment() {
            return null;
        }
    }

    public static final class ProfileScreen implements AppScreen {

        @NotNull
        @Override
        public String getKey() {
            return getClass().getCanonicalName();
        }

        @Nullable
        @Override
        public Intent getActivityIntent(@NotNull Context context) {
            return new Intent(context, ProfileActivity.class);
        }

        @Nullable
        @Override
        public Fragment getFragment() {
            return null;
        }
    }

    public static final class ProfileInfoScreen implements AppScreen {

        @NotNull
        @Override
        public String getKey() {
            return getClass().getCanonicalName();
        }

        @Nullable
        @Override
        public Fragment getFragment() {
            return new ProfileFragment();
        }

        @Nullable
        @Override
        public Intent getActivityIntent(@NotNull Context context) {
            return null;
        }
    }

    public static final class SelectPhotoScreen implements AppScreen {

        @NotNull
        @Override
        public String getKey() {
            return getClass().getCanonicalName();
        }

        @Nullable
        @Override
        public Fragment getFragment() {
            return new SelectPhotoFragment();
        }

        @Nullable
        @Override
        public Intent getActivityIntent(@NotNull Context context) {
            return null;
        }
    }
}
