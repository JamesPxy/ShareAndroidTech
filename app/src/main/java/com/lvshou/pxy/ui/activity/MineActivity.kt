package com.lvshou.pxy.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.lvshou.pxy.R
import com.lvshou.pxy.base.BaseActivity
import com.lvshou.pxy.utils.TestTimerTask
import kotlinx.android.synthetic.main.activity_mine.*
import toast

class MineActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tvContent -> {
                startActivity(Intent(this@MineActivity, TestTimerTask::class.java))
                finish()
            }
        }
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
        toolbar.titleMarginStart = 0
        toolbar.setTitleTextColor(Color.RED)
        tvContent.setOnClickListener(this)

        textPathView1.setText("很划算的几乎将户籍卡hi回馈于胡gagjdasg赶回家卡嘎四大金刚的沮丧激活工具感觉很·")

        textPathView1.startAnim()
       /* var progress=0f
        Timer()?.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    progress++
                    textPathView1.progress=progress
                }
            }
        }, 2000, 1000)*/
    }


    override fun setLayoutId(): Int = R.layout.activity_mine

    override fun cancelRequest() {
    }


}
