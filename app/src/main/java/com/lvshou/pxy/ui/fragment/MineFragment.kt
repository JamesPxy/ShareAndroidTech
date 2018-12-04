package com.lvshou.pxy.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lvshou.pxy.R
import com.lvshou.pxy.base.BaseFragment
import com.lvshou.pxy.ui.activity.MineActivity
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvGoCenter.setOnClickListener {
            startActivity(Intent(activity, MineActivity::class.java))
//            startActivity(Intent(activity, TestScrollViewActivity::class.java))
        }
    }

    override fun cancelRequest() {
    }
}