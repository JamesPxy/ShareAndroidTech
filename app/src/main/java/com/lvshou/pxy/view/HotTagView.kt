package com.lvshou.pxy.view

import com.lvshou.pxy.bean.HotKeyResponse

/**
 * @desc：
 * Created by JamesPxy on 2018/6/13 14:52
 */
interface HotTagView {

    fun getHotTagSuccess(hotResult: HotKeyResponse)

    fun getHotTagFailed(errorMessage: String?)
}