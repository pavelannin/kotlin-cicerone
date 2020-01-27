package ru.terrakok.cicerone

/**
 * Cicerone is the holder for other library components.
 * When you need a [NavigatorHolder] or [Router], get it here.
 */
class Cicerone<R : Router>(val router: R, val navigatorHolder: NavigatorHolder) {

    companion object {

        @JvmStatic
        fun <R : Router> create(routerCreator: (CommandExecutable) -> R): Cicerone<R> {
            val commandBuffer = CommandBuffer()
            return Cicerone(router = routerCreator(commandBuffer), navigatorHolder = commandBuffer)
        }

        @JvmStatic
        fun create(): Cicerone<DefaultRouter> {
            return create { DefaultRouter(executor = it) }
        }
    }
}
