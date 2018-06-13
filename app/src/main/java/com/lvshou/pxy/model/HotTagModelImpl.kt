package com.lvshou.pxy.model

import RetrofitHelper
import cancelByActive
import com.lvshou.pxy.presenter.HotTagPresenter
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import top.jowanxu.wanandroidclient.bean.HotKeyResponse
import tryCatch

/**
 * @desc：
 * Created by JamesPxy on 2018/6/13 14:41
 */
class HotTagModelImpl : HotTagModel {

    /**
     * Friend list async
     */
    private var getHotTagAsync: Deferred<HotKeyResponse>? = null

    override fun getHotTag(presenter: HotTagPresenter) {
        var throwable: Throwable? = null
        var hotResult: HotKeyResponse? = null
        async(UI) {
            tryCatch({
                throwable = it
                it.printStackTrace()
            }) {
                getHotTagAsync?.cancelByActive()
                getHotTagAsync = RetrofitHelper.retrofitService.getHotKeyList()
                val result = getHotTagAsync?.await()
                result?.let {
                    hotResult = it
                }
            }
            throwable?.let {
                presenter.getHotTagFailed(it.toString())
                return@async
            }
            presenter.getHotTagSuccess(hotResult!!)
        }
    }

    /**
     * 取消网络请求
     */
    override fun cancelRequest() {
        getHotTagAsync?.cancelByActive()
    }
}