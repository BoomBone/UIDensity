package com.ticccccc.uidesnitytest

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ticccccc.uidesnitytest.ui.UIUtils
import com.ticccccc.uidesnitytest.ui.ViewCalculateUtil
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        UIUtils.getInstance(applicationContext)
        initView()
    }

    private fun initView() {
        ViewCalculateUtil.setViewLayoutParam(mViewOne, 270, 270, 0, 0, 0, 0)
        ViewCalculateUtil.setViewLayoutParam(mViewTwo, 810, 270, 0, 0, 0, 0)
        ViewCalculateUtil.setViewLayoutParam(mViewThree, 540, 270, 0, 0, 0, 0)
        ViewCalculateUtil.setViewLayoutParam(mViewFour, 1080, 270, 0, 0, 0, 0)
    }
}
