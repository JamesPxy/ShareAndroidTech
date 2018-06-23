package com.lvshou.pxy.presenter

import com.lvshou.pxy.bean.HomeListResponse
import com.lvshou.pxy.model.CollectOutsideModelImpl
import com.lvshou.pxy.view.CollectArticleView
import java.util.*

/**
 * @desc：
 * Created by JamesPxy on 2018/6/21 17:28
 */
class CollectOutsidePresenterImpl(private val view: CollectArticleView) : CollectOutsidePresenter {

    private val model: CollectOutsideModelImpl = CollectOutsideModelImpl()

    override fun collectOutSideArticle(id: Int, title: String, author: String, link: String, isAdd: Boolean) {
        model.collectOutSideArticle(id, title, author, link, isAdd, this)
    }

    override fun collectOutsideArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        if (0 != result.errorCode) {
            view.collectArticleFailed(result.errorMsg, isAdd)
        } else {
            view.collectArticleSuccess(result, isAdd)
        }
    }

    override fun collectOutsideArticleFailed(errorMessage: String?, isAdd: Boolean) {
        view.collectArticleFailed(errorMessage, isAdd)
    }
}