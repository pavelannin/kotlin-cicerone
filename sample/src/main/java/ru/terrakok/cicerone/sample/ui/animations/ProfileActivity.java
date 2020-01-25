package ru.terrakok.cicerone.sample.ui.animations;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.transition.ChangeBounds;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.AppNavigatorImpl;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Forward;
import ru.terrakok.cicerone.commands.Replace;
import ru.terrakok.cicerone.sample.R;
import ru.terrakok.cicerone.sample.SampleApplication;
import ru.terrakok.cicerone.sample.Screens;
import ru.terrakok.cicerone.sample.ui.animations.photos.SelectPhotoFragment;
import ru.terrakok.cicerone.sample.ui.animations.profile.ProfileFragment;
import ru.terrakok.cicerone.sample.ui.common.BackButtonListener;

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 14.07.17.
 */

public class ProfileActivity extends AppCompatActivity {
    public static final String PHOTO_TRANSITION = "photo_trasition";

    @Inject
    NavigatorHolder navigatorHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        SampleApplication.INSTANCE.getAppComponent().inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_container);

        if (savedInstanceState == null) {
            navigator.applyCommands(new Command[]{new Replace(Screens.ProfileInfoScreen.INSTANCE)});
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    private final AppNavigatorImpl.Middleware navigatorMiddleware = new AppNavigatorImpl.Middleware() {

        @Nullable
        @Override
        public Bundle createActivityOptions(@NotNull Command command, @NotNull Intent activityIntent) {
            return null;
        }

        @NotNull
        @Override
        public FragmentTransaction setupFragmentTransaction(@NotNull Command command, @Nullable Fragment currentFragment, @NotNull Fragment nextFragment, @NotNull FragmentTransaction fragmentTransaction) {
            if (command instanceof Forward
                    && currentFragment instanceof ProfileFragment
                    && nextFragment instanceof SelectPhotoFragment) {
                setupSharedElementForProfileToSelectPhoto(
                        (ProfileFragment) currentFragment,
                        (SelectPhotoFragment) nextFragment,
                        fragmentTransaction
                );
            }
            return fragmentTransaction;
        }
    };

    private Navigator navigator = new AppNavigatorImpl(this, getSupportFragmentManager(), R.id.container, navigatorMiddleware);

    private void setupSharedElementForProfileToSelectPhoto(ProfileFragment profileFragment,
                                                           SelectPhotoFragment selectPhotoFragment,
                                                           FragmentTransaction fragmentTransaction) {
        ChangeBounds changeBounds = new ChangeBounds();
        selectPhotoFragment.setSharedElementEnterTransition(changeBounds);
        selectPhotoFragment.setSharedElementReturnTransition(changeBounds);
        profileFragment.setSharedElementEnterTransition(changeBounds);
        profileFragment.setSharedElementReturnTransition(changeBounds);

        View view = profileFragment.getAvatarViewForAnimation();
        fragmentTransaction.addSharedElement(view, PHOTO_TRANSITION);
        selectPhotoFragment.setAnimationDestinationId((Integer) view.getTag());
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment != null
                && fragment instanceof BackButtonListener
                && ((BackButtonListener) fragment).onBackPressed()) {
            return;
        } else {
            super.onBackPressed();
        }
    }
}
