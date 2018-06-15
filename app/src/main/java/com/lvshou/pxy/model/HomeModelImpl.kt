package com.lvshou.pxy.model

import RetrofitHelper
import cancelByActive
import com.lvshou.pxy.bean.BannerResponse
import com.lvshou.pxy.presenter.HomePresenter
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import top.jowanxu.wanandroidclient.bean.HomeListResponse
import tryCatch

/**
 * @desc：
 * Created by JamesPxy on 2018/6/15 11:12
 */
class HomeModelImpl : HomeModel {

//    private var getBannerAsync: Deferred<BannerResponse>? = null

    private var getHomeListAsync: Deferred<HomeListResponse>? = null

    private val getBannerAsync: Deferred<BannerResponse> by lazy {
        RetrofitHelper.retrofitService.getBanner()
    }
//    private val getHomeListAsync: Deferred<HomeListResponse> by lazy {
//        RetrofitHelper.retrofitService.getHomeList(0)
//    }

    override fun getHomeBanner(presenter: HomePresenter) {
        async(UI) {
            tryCatch({
                presenter.getHomeBannerFailed(it.localizedMessage)
                it.printStackTrace()
            }) {
//                getBannerAsync.cancelByActive()
                var result = getBannerAsync.await()
                result?.let {
                    presenter.getHomeBannerSuccess(result)
                }/*?.let {
                    presenter.getHomeListFailed("返回数据为空")
                }*/
            }
        }
    }

    override fun getHomeList(page: Int, presenter: HomePresenter) {
        async(UI) {
            tryCatch({
                presenter.getHomeListFailed(it.localizedMessage)
                it.printStackTrace()
            }) {
                getHomeListAsync?.cancelByActive()
                getHomeListAsync = RetrofitHelper.retrofitService.getHomeList(page)
                var result = getHomeListAsync?.await()
                presenter.getHomeListSuccess(result)
//                result?.let {
//                    presenter.getHomeListSuccess(result)
//                }?.let {
//                    presenter.getHomeListFailed("返回数据为空")
//                }
            }
        }
    }

    override fun cancelRequest() {
        getBannerAsync.cancelByActive()
        getHomeListAsync.cancelByActive()
    }

}