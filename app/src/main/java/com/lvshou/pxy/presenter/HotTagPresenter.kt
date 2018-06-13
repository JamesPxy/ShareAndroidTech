package com.lvshou.pxy.presenter

import top.jowanxu.wanandroidclient.bean.HotKeyResponse

/**
 * @desc：
 * Created by JamesPxy on 2018/6/13 14:46
 */
interface HotTagPresenter {
    fun getHotTag()

    fun getHotTagSuccess(hotResult: HotKeyResponse)

    fun getHotTagFailed(errorMessage: String?)

    fun cancelRequest()
}