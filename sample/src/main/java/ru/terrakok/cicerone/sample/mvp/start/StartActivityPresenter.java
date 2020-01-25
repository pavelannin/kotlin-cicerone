package ru.terrakok.cicerone.sample.mvp.start;

import com.arellomobile.mvp.MvpPresenter;

import ru.terrakok.cicerone.AppRouter;
import ru.terrakok.cicerone.sample.Screens;

/**
 * Created by terrakok 21.11.16
 */
public class StartActivityPresenter extends MvpPresenter<StartActivityView> {
    private AppRouter router;

    public StartActivityPresenter(AppRouter router) {
        this.router = router;
    }

    public void onOrdinaryPressed() {
        router.navigateTo(Screens.MainScreen.INSTANCE);
    }

    public void onMultiPressed() {
        router.navigateTo(Screens.BottomNavigationScreen.INSTANCE);
    }

    public void onResultWithAnimationPressed() {
        router.navigateTo(Screens.ProfileScreen.INSTANCE);
    }

    public void onBackPressed() {
        router.exit();
    }
}
