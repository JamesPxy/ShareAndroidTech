package com.lvshou.pxy.model

import cancelByActive
import com.lvshou.pxy.bean.HomeListResponse
import com.lvshou.pxy.presenter.CollectArticlePresenter
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import tryCatch

/**
 * @desc：
 * Created by JamesPxy on 2018/6/21 14:57
 */
class CollectArticleModelImpl : CollectArticleModel {

    private var collectAsync: Deferred<HomeListResponse>? = null

    override fun collectArticle(id: Int, isAdd: Boolean, presenter: CollectArticlePresenter) {
        async(UI) {
            tryCatch({
                presenter.collectArticleFailed(it.localizedMessage,isAdd)
                it.printStackTrace()
            }){
                collectAsync?.cancelByActive()
                collectAsync = if(isAdd) {
                    RetrofitHelper.retrofitService.addCollectArticle(id)
                }else{
                    RetrofitHelper.retrofitService.removeCollectArticle(id)
                }
                var result=collectAsync?.await()
                result?.let {
                    presenter.collectArticleSuccess(result,isAdd)
                }
            }
        }
    }
}