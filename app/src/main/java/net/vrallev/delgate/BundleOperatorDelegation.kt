package net.vrallev.delgate

import android.os.Bundle
import androidx.core.os.bundleOf
import kotlin.reflect.KProperty

class User(bundle: Bundle) {
    val name: String by bundle
    val age: Int     by bundle
}

val user = User(bundleOf(
    "name" to "John Doe",
    "age" to 25
))

@Suppress("IMPLICIT_CAST_TO_ANY")
inline operator fun <reified V : Any> Bundle.getValue(
    thisRef: Any?,
    property: KProperty<*>
): V = when (V::class) {
    String::class -> this.getString(property.name)
    Int::class -> this.getInt(property.name)
    else -> throw NotImplementedError("Missing for ${V::class}")
} as V