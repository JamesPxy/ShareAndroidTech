package com.lvshou.pxy.presenter

import com.lvshou.pxy.bean.HomeListResponse


/**
 * @desc：
 * Created by JamesPxy on 2018/6/21 14:45
 */
interface CollectArticlePresenter {

    fun collectArticle(id: Int, isAdd: Boolean)

    fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean)

    fun collectArticleFailed(errorMessage: String?, isAdd: Boolean)
}