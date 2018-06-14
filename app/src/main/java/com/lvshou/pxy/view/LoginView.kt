package com.lvshou.pxy.view

import top.jowanxu.wanandroidclient.bean.LoginResponse

/**
 * @desc：
 * Created by JamesPxy on 2018/6/14 14:55
 */
interface LoginView {
    fun loginSuccess(response: LoginResponse)

    fun loginFailed(errorMsg: String)

    fun registerSuccess(response: LoginResponse)

    fun registerFailed(errorMsg: String)

    fun showProgressBar()

    fun hideProgressBar()

    fun checkIsInvalidedInfo(): Boolean

    fun startMainActivity()
}