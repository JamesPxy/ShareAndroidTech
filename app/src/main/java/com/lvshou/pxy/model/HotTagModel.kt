package com.lvshou.pxy.model

import com.lvshou.pxy.presenter.HotTagPresenter

/**
 * @desc：
 * Created by JamesPxy on 2018/6/13 14:39
 */
interface HotTagModel {

    fun getHotTag(presenter: HotTagPresenter)

    fun cancelRequest()

}