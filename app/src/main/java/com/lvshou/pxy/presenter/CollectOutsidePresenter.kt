package com.lvshou.pxy.presenter

import com.lvshou.pxy.bean.HomeListResponse

/**
 * @desc：
 * Created by JamesPxy on 2018/6/21 16:38
 */
interface CollectOutsidePresenter {

    /**
     *  add or remove outside collect article
     *  @param title article title
     *  @param author article author
     *  @param link article link
     *  @param isAdd true add, false remove
     */
    fun collectOutSideArticle(id: Int, title: String, author: String, link: String, isAdd: Boolean)

    /**
     * add collect outside article success
     * @param result HomeListResponse
     * @param isAdd true add, false remove
     */
    fun collectOutsideArticleSuccess(result: HomeListResponse, isAdd: Boolean)

    /**
     * add collect outside article failed
     * @param errorMessage error message
     * @param isAdd true add, false remove
     */
    fun collectOutsideArticleFailed(errorMessage: String?, isAdd: Boolean)

}