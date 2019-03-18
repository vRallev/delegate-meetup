package net.vrallev.delgate

import android.app.Activity
import android.view.View
import androidx.annotation.IdRes
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private class ViewDelegate<T : View>(
    @IdRes private val viewId: Int
) : ReadOnlyProperty<Activity, T> {

    private var view: T? = null

    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        return view ?: thisRef.findViewById<T>(viewId).also { view = it }
    }
}

fun <T : View> view(@IdRes viewId: Int): ReadOnlyProperty<Activity, T> = ViewDelegate(viewId)