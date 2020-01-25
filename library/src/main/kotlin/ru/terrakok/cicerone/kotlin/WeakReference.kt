@file:JvmMultifileClass

package ru.terrakok.cicerone.kotlin

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import java.lang.ref.WeakReference as JavaWeakReference

internal class WeakReference<T>(value: T? = null) : ReadWriteProperty<Any, T?> {

    private var weak: JavaWeakReference<T>? = value?.let { JavaWeakReference(it) }

    override fun getValue(thisRef: Any, property: KProperty<*>): T? {
        return weak?.get()
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T?) {
        weak = value?.let { JavaWeakReference(it) }
    }
}

/** @author П. Аннин. */
internal fun <T> weak(value: T?): WeakReference<T> = WeakReference(value)