package ru.terrakok.cicerone

/**
 * Screen is interface for description application screen.
 *
 * Support @JvmName on interface.
 * https://youtrack.jetbrains.com/issue/KT-31420
 */
@Suppress(names = ["INAPPLICABLE_JVM_NAME"])
interface Screen {

    @get:JvmName(name = "getKey") val key: String
}
