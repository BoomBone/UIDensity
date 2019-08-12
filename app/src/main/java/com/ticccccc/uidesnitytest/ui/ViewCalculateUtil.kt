package com.ticccccc.uidesnitytest.ui

import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

/**
 * Created by Ting on 2019-08-11.
 */
object ViewCalculateUtil {
    fun setViewLayoutParam(
        view: View,
        width: Int,
        height: Int,
        topMargin: Int,
        bottomMargin: Int,
        leftMargin: Int,
        rightMargin: Int
    ) {
        val layoutParams = view.layoutParams as RelativeLayout.LayoutParams
        if (width != RelativeLayout.LayoutParams.MATCH_PARENT
            && width != RelativeLayout.LayoutParams.WRAP_CONTENT
            && width != RelativeLayout.LayoutParams.FILL_PARENT
        ) {
            layoutParams.width = UIUtils.getInstance().getWidth(width)
        } else {
            layoutParams.width = width
        }
        if (height != RelativeLayout.LayoutParams.MATCH_PARENT
            && height != RelativeLayout.LayoutParams.WRAP_CONTENT
            && height != RelativeLayout.LayoutParams.FILL_PARENT
        ) {
            layoutParams.height = UIUtils.getInstance().getWidth(height)
        } else {
            layoutParams.height = height
        }
        layoutParams.topMargin = UIUtils.getInstance().getHeight(topMargin)
        layoutParams.bottomMargin = UIUtils.getInstance().getHeight(bottomMargin)
        layoutParams.leftMargin = UIUtils.getInstance().getHeight(leftMargin)
        layoutParams.rightMargin = UIUtils.getInstance().getHeight(rightMargin)
        view.layoutParams = layoutParams
    }

    fun setTextSize(view:TextView,size:Int){
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX,UIUtils.getInstance().getHeight(size).toFloat())
    }

}