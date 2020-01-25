package ru.terrakok.cicerone.sample.subnavigation;

import java.util.HashMap;

import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.AppRouter;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

/**
 * Created by terrakok 27.11.16
 */
public class LocalCiceroneHolder {
    private HashMap<String, Cicerone<AppRouter>> containers;

    public LocalCiceroneHolder() {
        containers = new HashMap<>();
    }

    public Cicerone<AppRouter> getCicerone(String containerTag) {
        if (!containers.containsKey(containerTag)) {
            containers.put(containerTag, Cicerone.create());
        }
        return containers.get(containerTag);
    }
}
