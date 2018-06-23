package com.lvshou.pxy.view

import com.lvshou.pxy.bean.HomeListResponse

/**
 * @desc：
 * Created by JamesPxy on 2018/6/21 14:39
 */
interface CollectArticleView {
    /**
     * add article success
     * @param result HomeListResponse
     * @param isAdd true add, false remove
     */
    fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean)

    /**
     * add article false
     * @param errorMessage error message
     * @param isAdd true add, false remove
     */
    fun collectArticleFailed(errorMessage: String?, isAdd: Boolean)

}