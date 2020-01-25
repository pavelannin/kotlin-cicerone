/*
 * Created by Konstantin Tskhovrebov (aka @terrakok)
 */
package ru.terrakok.cicerone

import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.kotlin.weak
import java.util.LinkedList
import java.util.Queue

/**
 * Passes navigation command to an active [Navigator]
 * or stores it in the pending commands queue to pass it later.
 */
class CommandBuffer : CommandExecutable, NavigatorHolder {

    private val pendingCommands: Queue<Array<out Command>> by lazy { LinkedList<Array<out Command>>() }
    private var _navigator: Navigator? by weak<Navigator>(value = null)

    override fun setNavigator(navigator: Navigator?) {
        _navigator = navigator
        while (!pendingCommands.isEmpty()) {
            _navigator?.let { execute(pendingCommands.poll()) } ?: break
        }
    }

    override fun removeNavigator() {
        _navigator = null
    }

    // /**
    //  * Passes `commands` to the [Navigator] if it available.
    //  * Else puts it to the pending commands queue to pass it later.
    //  * @param commands navigation command array
    //  */
    // fun executeCommands(commands: Array<out Command>) {
    //
    // }

    override fun execute(commands: Array<out Command>) {
        _navigator?.apply(commands) ?: pendingCommands.add(commands)
    }
}
