package net.vrallev.delgate

import java.lang.reflect.Proxy

interface MyInterface {
    fun something()
}

class Unimplemented : MyInterface by TODO()

inline fun <reified T : Any> TODO(): T {
    val clazz = T::class.java
    return Proxy.newProxyInstance(clazz.classLoader, arrayOf(clazz)) { _, method, _ ->
        kotlin.TODO("Method ${method.name}() not implemented")
    } as T
}
