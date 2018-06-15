package com.lvshou.pxy.presenter

import com.lvshou.pxy.bean.BannerResponse
import top.jowanxu.wanandroidclient.bean.HomeListResponse

/**
 * @desc：
 * Created by JamesPxy on 2018/6/15 11:08
 */
interface HomePresenter {

    fun getHomeBanner()

    fun getHomeList(page: Int = 0)

    fun getHomeBannerSuccess(response: BannerResponse)

    fun getHomeBannerFailed(errorMsg: String)

    fun getHomeListSuccess(response: HomeListResponse?)

    fun getHomeListFailed(errorMsg: String)

    fun cancelRequest()

}