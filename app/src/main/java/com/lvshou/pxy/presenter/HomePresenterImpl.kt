package com.lvshou.pxy.presenter

import com.lvshou.pxy.bean.BannerResponse
import com.lvshou.pxy.model.HomeModelImpl
import com.lvshou.pxy.view.HomeFragmentView
import top.jowanxu.wanandroidclient.bean.HomeListResponse

/**
 * @desc：
 * Created by JamesPxy on 2018/6/15 12:05
 */
class HomePresenterImpl(var homeView: HomeFragmentView) : HomePresenter {

    private val homeModel = HomeModelImpl()

    override fun getHomeBanner() {
        homeModel.getHomeBanner(this)
    }

    override fun getHomeList(page: Int) {
        homeModel.getHomeList(page, this)
    }

    override fun getHomeBannerSuccess(response: BannerResponse) {
        if (0 != response.errorCode) {
            response.errorMsg?.let { homeView.getHomeBannerFailed(it) }
            return
        }
        homeView.getHomeBannerSuccess(response)
    }

    override fun getHomeBannerFailed(errorMsg: String) {
        homeView.getHomeBannerFailed(errorMsg)
    }

    override fun getHomeListSuccess(response: HomeListResponse?) {
        if (0 != response?.errorCode) {
            response?.errorMsg?.let { homeView.getHomeBannerFailed(it) }
            return
        }
        homeView.getHomeListSuccess(response)
    }

    override fun getHomeListFailed(errorMsg: String) {
        homeView.getHomeBannerFailed(errorMsg)
    }

    override fun cancelRequest() {
        homeModel.cancelRequest()
    }

}