package com.lvshou.pxy.model

import com.lvshou.pxy.presenter.HomePresenter

/**
 * @desc：
 * Created by JamesPxy on 2018/6/15 11:11
 */
interface HomeModel {

    fun getHomeBanner(presenter: HomePresenter)

    fun getHomeList(page: Int, presenter: HomePresenter)

    fun cancelRequest()
}