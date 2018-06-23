package com.lvshou.pxy.presenter

import com.lvshou.pxy.bean.HomeListResponse
import com.lvshou.pxy.model.CollectArticleModelImpl
import com.lvshou.pxy.view.CollectArticleView

/**
 * @desc：
 * Created by JamesPxy on 2018/6/21 15:10
 */
class CollectArticlePresenterImpl(private val collectArticleView: CollectArticleView) : CollectArticlePresenter {

    private val model: CollectArticleModelImpl by lazy {
        CollectArticleModelImpl()
    }

    override fun collectArticle(id: Int, isAdd: Boolean) {
        model.collectArticle(id, isAdd, this)
    }

    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        result.let {
            if (0 != result.errorCode) {
                collectArticleView.collectArticleFailed(it.errorMsg, isAdd)
            } else {
                collectArticleView.collectArticleSuccess(it, isAdd)
            }
        }
    }

    override fun collectArticleFailed(errorMessage: String?, isAdd: Boolean) {
        collectArticleView.collectArticleFailed(errorMessage, isAdd)
    }

}