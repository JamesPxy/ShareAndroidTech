package com.lvshou.pxy.model

import cancelByActive
import com.lvshou.pxy.bean.HomeListResponse
import com.lvshou.pxy.presenter.CollectOutsidePresenter
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import tryCatch

/**
 * @desc：
 * Created by JamesPxy on 2018/6/21 17:03
 */
class CollectOutsideModelImpl : CollectOutsideModel {

    private var collectAsync: Deferred<HomeListResponse>? = null

    override fun collectOutSideArticle(id: Int, title: String, author: String, link: String, isAdd: Boolean, presenter: CollectOutsidePresenter) {
        async(UI) {
            tryCatch({
                presenter.collectOutsideArticleFailed(it.localizedMessage, isAdd)
                it.printStackTrace()
            }) {
                collectAsync?.cancelByActive()
                collectAsync = if (isAdd) {
                    RetrofitHelper.retrofitService.addCollectOutsideArticle(title, author, link)
                } else {
                    RetrofitHelper.retrofitService.removeCollectArticle(id)
                }
                var result = collectAsync?.await()
                result?.let {
                    presenter.collectOutsideArticleSuccess(result, isAdd)
                }
            }
        }
    }
}