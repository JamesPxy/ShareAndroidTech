package com.lvshou.pxy.model

import com.lvshou.pxy.presenter.CollectArticlePresenter

/**
 * @desc：
 * Created by JamesPxy on 2018/6/21 14:56
 */
interface CollectArticleModel {

    fun collectArticle(id: Int, isAdd: Boolean, presenter: CollectArticlePresenter)

}