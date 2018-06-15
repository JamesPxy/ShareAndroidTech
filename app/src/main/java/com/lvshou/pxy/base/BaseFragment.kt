package com.lvshou.pxy.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment

/**
 * @desc：Fragment 基类
 * Created by JamesPxy on 2018/6/13 10:58
 */
abstract class BaseFragment : Fragment() {

    protected lateinit var mActivity: Activity
    /**
     * cancel request
     */
    protected abstract fun cancelRequest()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = activity!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancelRequest()
    }
}