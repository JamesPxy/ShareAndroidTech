package com.lvshou.pxy.model

import com.lvshou.pxy.presenter.CollectOutsidePresenter

/**
 * @desc：
 * Created by JamesPxy on 2018/6/21 16:42
 */
interface CollectOutsideModel {

    /**
     *  add or remove outside collect article
     *  @param id  article id
     *  @param title article title
     *  @param author article author
     *  @param link article link
     *  @param isAdd true add, false remove
     *  @param presenter
     */
    fun collectOutSideArticle(id: Int,title: String, author: String, link: String, isAdd: Boolean, presenter: CollectOutsidePresenter)
}