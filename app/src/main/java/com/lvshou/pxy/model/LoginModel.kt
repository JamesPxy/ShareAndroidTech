package com.lvshou.pxy.model

import com.lvshou.pxy.presenter.LoginPresenter

/**
 * @desc：
 * Created by JamesPxy on 2018/6/14 15:04
 */
interface LoginModel {
    fun login(username: String, pwd: String, presenter: LoginPresenter)

    fun register(username: String, pwd: String, presenter: LoginPresenter)

    fun cancelRequest()
}