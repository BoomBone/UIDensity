package com.ticccccc.uidesnitytest.ui

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration

/**
 * Created by Ting on 2019-05-05.
 * 屏幕适配修改系统desnsity
 */
object DensityUtil {

    //参考设备的宽度,单位dp
    private const val WIDTH = 1920f
    private var appDensity = 0f
    private var appScaleDensity = 0f

    fun setDensity(application: Application, activity: Activity) {
        //获取当前app屏幕信息
        val displayMetrics = application.resources.displayMetrics
        if (appDensity == 0f) {
            //初始化赋值操作
            appDensity = displayMetrics.density
            appScaleDensity = displayMetrics.scaledDensity

            //计算目标值的density，scaleDensity，densityDpi，获取到的单位是px
            val targetDensity = displayMetrics.widthPixels / WIDTH
            val targetScaleDensity = targetDensity * (appScaleDensity / appDensity)
            val targetDensityDpi = (targetDensity * 160).toInt()

            //添加字体变化监听回调
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onLowMemory() {
                }

                override fun onConfigurationChanged(newConfig: Configuration?) {
                    //字体发生更改，重新对scaleDensity进行赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaleDensity = application.resources.displayMetrics.scaledDensity
                    }
                }

            })

            //替换Activity的density，scaleDensity,densityDpi
            val dm = activity.resources.displayMetrics
            dm.density = targetDensity
            dm.scaledDensity = targetScaleDensity
            dm.densityDpi = targetDensityDpi
        }
    }

}

