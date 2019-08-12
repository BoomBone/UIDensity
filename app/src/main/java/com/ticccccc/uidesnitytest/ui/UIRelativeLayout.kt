package com.ticccccc.uidesnitytest.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**
 * Created by Ting on 2019-08-12.
 */
class UIRelativeLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var flag = true

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (flag){
            flag = false

            val scaleX = UIUtils.getInstance(context).getHorizontalScaleValue()
            val childCount = this.childCount
            for (i in 0 until childCount){
                val child = this.getChildAt(i)
                val layoutParams = child.layoutParams as LayoutParams

                layoutParams.width= (layoutParams.width*scaleX).toInt()
                layoutParams.height= (layoutParams.height*scaleX).toInt()
                layoutParams.leftMargin= (layoutParams.leftMargin*scaleX).toInt()
                layoutParams.rightMargin= (layoutParams.rightMargin*scaleX).toInt()
                layoutParams.topMargin= (layoutParams.topMargin*scaleX).toInt()
                layoutParams.bottomMargin= (layoutParams.bottomMargin*scaleX).toInt()
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }
}