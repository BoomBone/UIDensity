package com.ticccccc.uidesnitytest

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.ticccccc.uidesnitytest.ui.DensityUtil

/**
 * Created by Ting on 2019-08-12.
 */
class AppApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initDensity()
    }

    private fun initDensity() {
        registerActivityLifecycleCallbacks(object :ActivityLifecycleCallbacks{
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {
            }

            override fun onActivityDestroyed(activity: Activity?) {
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {
            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                DensityUtil.setDensity(this@AppApplication,activity!!)
            }

        })
    }
}