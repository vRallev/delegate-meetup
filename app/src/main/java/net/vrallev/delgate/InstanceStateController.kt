package net.vrallev.delgate

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.SparseArray
import androidx.core.util.set

interface InstanceStateProperty {
    fun restore(savedInstanceState: Bundle)
    fun save(outState: Bundle)
}

object InstanceStateController {
    private val delegateMap = SparseArray<MutableSet<InstanceStateProperty>?>()
    private val lastInstanceStates = SparseArray<Bundle?>()

    fun create(application: Application) {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {

            override fun onActivityStarted(activity: Activity) = Unit
            override fun onActivityResumed(activity: Activity) = Unit
            override fun onActivityPaused(activity: Activity) = Unit
            override fun onActivityStopped(activity: Activity) = Unit

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                if (savedInstanceState != null) {
                    lastInstanceStates[activity.key] = savedInstanceState
                    delegateMap[activity.key]?.forEach { it.restore(savedInstanceState) }
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                delegateMap[activity.key]?.forEach { it.save(outState) }
            }

            override fun onActivityDestroyed(activity: Activity) {
                delegateMap.remove(activity.key)
                delegateMap.remove(activity.key)
            }
        })
    }

    fun register(activity: Activity, delegate: InstanceStateProperty) {
        if (activity.isDestroyed) return

        val delegates = delegateMap.get(activity.key)
            ?: mutableSetOf<InstanceStateProperty>().also { delegateMap[activity.key] = it }

        if (!delegates.contains(delegate)) {
            delegates += delegate

            lastInstanceStates[activity.key]?.let { delegate.restore(it) }
        }
    }

    private val Activity.key: Int get() = hashCode()
}
