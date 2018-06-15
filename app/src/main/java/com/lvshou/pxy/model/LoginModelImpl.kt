package com.lvshou.pxy.model

import RetrofitHelper
import cancelByActive
import com.lvshou.pxy.presenter.LoginPresenter
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import top.jowanxu.wanandroidclient.bean.LoginResponse
import tryCatch

/**
 * @desc：登录/注册 model实现类
 * Created by JamesPxy on 2018/6/14 15:06
 */
class LoginModelImpl : LoginModel {

    /**
     * Login async
     */
    private var loginAsync: Deferred<LoginResponse>? = null
    /**
     * Register async
     */
    private var registerAsync: Deferred<LoginResponse>? = null

    override fun login(username: String, pwd: String, presenter: LoginPresenter) {
        async(UI) {
            tryCatch({
                presenter.loginFailed(it.localizedMessage)
                it.printStackTrace()
            }) {
                loginAsync?.cancelByActive()
                loginAsync = RetrofitHelper.retrofitService.loginWanAndroid(username, pwd)
                val result = loginAsync?.await()
                result?.let {
                    presenter.loginSuccess(it)
                }?.let {
                    presenter.loginFailed("返回数据为空")
                }
            }
        }
    }

    override fun register(username: String, pwd: String, presenter: LoginPresenter) {
        async(UI) {
            tryCatch({
                presenter.registerFailed(it.localizedMessage)
                it.printStackTrace()
            }) {
                registerAsync?.cancelByActive()
                registerAsync = RetrofitHelper.retrofitService.registerWanAndroid(username, pwd, pwd)
                val result = registerAsync?.await()
                result?.let {
                    presenter.loginSuccess(result)
                }?.let {
                    presenter.registerFailed("返回数据为空")
                }
            }
        }
    }

    override fun cancelRequest() {
        loginAsync?.cancelByActive()
        registerAsync?.cancelByActive()
    }
}