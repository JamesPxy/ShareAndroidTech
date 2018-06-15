package com.lvshou.pxy.view

import com.lvshou.pxy.bean.BannerResponse
import top.jowanxu.wanandroidclient.bean.HomeListResponse

/**
 * @desc：
 * Created by JamesPxy on 2018/6/15 11:04
 */
interface HomeFragmentView {

    fun getHomeBannerSuccess(response: BannerResponse)

    fun getHomeBannerFailed(errorMsg: String)

    fun getHomeListSuccess(response: HomeListResponse?)

    fun getHomeListFailed(errorMsg: String)
}