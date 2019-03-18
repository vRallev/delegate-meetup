package net.vrallev.delgate

import android.app.Activity
import android.os.Bundle
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
fun <T : Any> instanceState(defaultValue: T): ReadWriteProperty<Activity, T> {
    return when (defaultValue) {
        is Int -> IntInstanceStateProperty(defaultValue)
        is String -> StringInstanceStateProperty(defaultValue)
        else -> throw NotImplementedError("Missing implementation for ${defaultValue::class}.")
    } as ReadWriteProperty<Activity, T>
}

private class IntInstanceStateProperty(defaultValue: Int) : BaseInstanceStateProperty<Int>(defaultValue) {
    override fun fromBundle(bundle: Bundle, key: String, defaultValue: Int): Int = bundle.getInt(key, defaultValue)
    override fun saveInBundle(bundle: Bundle, key: String, value: Int) {
        bundle.putInt(key, value)
    }
}

private class StringInstanceStateProperty(defaultValue: String) : BaseInstanceStateProperty<String>(defaultValue) {
    override fun fromBundle(bundle: Bundle, key: String, defaultValue: String): String = bundle.getString(key, defaultValue)
    override fun saveInBundle(bundle: Bundle, key: String, value: String) {
        bundle.putString(key, value)
    }
}

private sealed class BaseInstanceStateProperty<T : Any>(
    private val defaultValue: T
) : ReadWriteProperty<Activity, T>, InstanceStateProperty {

    private var value = defaultValue
    private var key: String? = null
    private var savedInstanceState: Bundle? = null

    abstract fun fromBundle(bundle: Bundle, key: String, defaultValue: T): T
    abstract fun saveInBundle(bundle: Bundle, key: String, value: T)

    final override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        initializeValue(thisRef, property)
        return value
    }

    final override fun setValue(thisRef: Activity, property: KProperty<*>, value: T) {
        initializeValue(thisRef, property)
        this.value = value
    }

    final override fun restore(savedInstanceState: Bundle) {
        key?.let { key ->
            if (savedInstanceState.containsKey(key) && savedInstanceState.getInt(key) != value && value != defaultValue) {
                throw IllegalStateException("The value changed before it could have been restored, this could imply data loss.")
            }
        }

        this.savedInstanceState = savedInstanceState
    }

    final override fun save(outState: Bundle) {
        key?.let { saveInBundle(outState, it, value) }
    }

    private fun initializeValue(thisRef: Activity, property: KProperty<*>) {
        key = key ?: property.toKey()
        InstanceStateController.register(thisRef, this)

        val bundle = savedInstanceState
        if (bundle != null) {
            value = fromBundle(bundle, key!!, defaultValue)
            savedInstanceState = null
        }
    }

    private fun KProperty<*>.toKey(): String = this.name
}

