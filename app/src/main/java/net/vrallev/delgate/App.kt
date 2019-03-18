package net.vrallev.delgate

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        InstanceStateController.create(this)
    }
}