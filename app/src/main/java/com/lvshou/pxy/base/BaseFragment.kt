package com.lvshou.pxy.base

import android.support.v4.app.Fragment

/**
 * @desc：Fragment 基类
 * Created by JamesPxy on 2018/6/13 10:58
 */
abstract class BaseFragment : Fragment() {

    /**
     * cancel request
     */
    protected abstract fun cancelRequest()

    override fun onDestroyView() {
        super.onDestroyView()
        cancelRequest()
    }
}