package ru.terrakok.cicerone.sample.mvp.bottom;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;

import ru.terrakok.cicerone.DefaultRouter;

/**
 * Created by terrakok 25.11.16
 */
@InjectViewState
public class BottomNavigationPresenter extends MvpPresenter<BottomNavigationView> {
    private DefaultRouter router;

    public BottomNavigationPresenter(DefaultRouter router) {
        this.router = router;
    }

    public void onBackPressed() {
        router.exit();
    }
}
