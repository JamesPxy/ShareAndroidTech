package com.lvshou.pxy.presenter

import com.lvshou.pxy.bean.LoginResponse

/**
 * @desc：
 * Created by JamesPxy on 2018/6/14 15:00
 */
interface LoginPresenter {

    fun login(username: String, pwd: String)

    fun loginSuccess(response: LoginResponse)

    fun loginFailed(errorMessage: String)

    fun register(username: String, pwd: String)

    fun registerSuccess(response: LoginResponse)

    fun registerFailed(errorMessage: String)

    fun cancelRequest()
}