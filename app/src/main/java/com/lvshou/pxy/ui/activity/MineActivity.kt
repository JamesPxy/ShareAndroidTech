package com.lvshou.pxy.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import com.lvshou.pxy.R
import com.lvshou.pxy.base.BaseActivity
import kotlinx.android.synthetic.main.activity_mine.*
import toast

class MineActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
    }

    @SuppressLint("RestrictedApi")
    override fun init() {
        toolbar.title = "个人中心"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            toast("back")
        }
//        toolbar.apply {
//            titleMarginStart = 0
//            setTitleTextColor(Color.RED)
//            title="apply"
//            subtitle = "come on"
//        }
        toolbar.titleMarginStart=0
        toolbar.setTitleTextColor(Color.RED)

    }


    override fun setLayoutId(): Int = R.layout.activity_mine

    override fun cancelRequest() {
    }


}
