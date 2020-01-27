package ru.terrakok.cicerone

/**
 * Screen is interface for description and creation screen.
 * NOTE: If you have described the creation of Intent then Activity will be started.
 * Recommendation: Use Intents for launch external application.
 *
 *
 * Support @JvmName on interface.
 * https://youtrack.jetbrains.com/issue/KT-31420
 */
@Suppress(names = ["INAPPLICABLE_JVM_NAME"])
interface Screen {

    @get:JvmName(name = "getKey") val key: String
    @get:JvmName(name = "getCreatorFactory") val creatorFactory: CreatorFactory

    interface CreatorFactory
}
