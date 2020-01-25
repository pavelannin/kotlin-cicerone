package ru.terrakok.cicerone.sample.dagger.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.AppRouter;

/**
 * Created by terrakok 24.11.16
 */

@Module
public class NavigationModule {
    private Cicerone<AppRouter> cicerone;

    public NavigationModule() {
        cicerone = Cicerone.create();
    }

    @Provides
    @Singleton
    AppRouter provideRouter() {
        return cicerone.getRouter();
    }

    @Provides
    @Singleton
    NavigatorHolder provideNavigatorHolder() {
        return cicerone.getNavigatorHolder();
    }
}
