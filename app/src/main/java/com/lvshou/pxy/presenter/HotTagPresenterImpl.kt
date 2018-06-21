package com.lvshou.pxy.presenter

import com.lvshou.pxy.bean.HotKeyResponse
import com.lvshou.pxy.model.HotTagModelImpl
import com.lvshou.pxy.view.HotTagView

/**
 * @desc：
 * Created by JamesPxy on 2018/6/13 14:51
 */
class HotTagPresenterImpl(var hotTagView: HotTagView) : HotTagPresenter {

    private val hotTagModel: HotTagModelImpl = HotTagModelImpl()

    override fun getHotTag() {
        hotTagModel.getHotTag(this)
    }

    override fun getHotTagSuccess(hotResult: HotKeyResponse) {
        if (0 != (hotResult.errorCode)) {
            hotTagView.getHotTagFailed(hotResult.errorMsg)
            return
        }
        hotTagView.getHotTagSuccess(hotResult)
    }

    override fun getHotTagFailed(errorMessage: String?) {
        hotTagView.getHotTagFailed(errorMessage)
    }

    override fun cancelRequest() {
        hotTagModel.cancelRequest()
    }

}