package com.lvshou.pxy.view

import top.jowanxu.wanandroidclient.bean.HotKeyResponse

/**
 * @desc：
 * Created by JamesPxy on 2018/6/13 14:52
 */
interface HotTagView {

    fun getHotTagSuccess(hotResult: HotKeyResponse)

    fun getHotTagFailed(errorMessage: String?)
}