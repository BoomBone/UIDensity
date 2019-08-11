package com.ticccccc.uidesnitytest.ui

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Created by Ting on 2019-08-11.
 * 获取宽高缩放系数
 */

class UIUtils private constructor(context: Context) {

    private var displayMetricsWidth = 0f
    private var displayMetricsHeight = 0f

    init {
        //计算缩放系数
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        //得到设备的真实值
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetricsWidth = displayMetrics.widthPixels.toFloat()
        displayMetricsHeight = displayMetrics.heightPixels.toFloat()

    }

    /**
     * 获取横向缩放系数
     */
    fun getHorizontalScaleValue(): Float {
        return displayMetricsWidth / STANDARD_WIDTH
    }

    /**
     * 获取竖向缩放系数
     */
    fun getVerticalScaleValue(): Float {
        return displayMetricsHeight / STANDARD_HEIGHT
    }

    fun getWidth(width: Int): Int {
        return Math.round(width * getHorizontalScaleValue())
    }

    fun getHeight(height: Int): Int {
        return Math.round(height * getHorizontalScaleValue())
    }


    companion object {
        private lateinit var instance: UIUtils

        private const val STANDARD_WIDTH = 1080f
        private const val STANDARD_HEIGHT = 1920f


        fun getInstance():UIUtils{
            if (!::instance.isInitialized){
               throw Exception("UIUtils not init")
            }
            return instance
        }

        fun getInstance(context: Context): UIUtils {
            if (!::instance.isInitialized) {
                instance = UIUtils(context)
            }
            return instance
        }

        fun restartInstance(context: Context) {
            instance = UIUtils(context)
        }
    }
}