package com.lvshou.pxy.presenter

import com.lvshou.pxy.bean.LoginResponse
import com.lvshou.pxy.model.LoginModelImpl
import com.lvshou.pxy.view.LoginView

/**
 * @desc：
 * Created by JamesPxy on 2018/6/14 15:37
 */
class LoginPresenterImpl(var loginView: LoginView) : LoginPresenter {

    private val loginModel = LoginModelImpl()

    override fun login(username: String, pwd: String) {
        loginView.showProgressBar()
        loginModel.login(username, pwd, this)
    }

    override fun loginSuccess(response: LoginResponse) {
        loginView.hideProgressBar()
        if (0 != response.errorCode) {
            response.errorMsg?.let { loginView.loginFailed(it) }
            return
        }
        loginView.loginSuccess(response)
    }

    override fun loginFailed(errorMessage: String) {
        loginView.hideProgressBar()
        loginView.loginFailed(errorMessage)
    }

    override fun register(username: String, pwd: String) {
        loginView.showProgressBar()
        loginModel.register(username, pwd, this)
    }

    override fun registerSuccess(response: LoginResponse) {
        loginView.hideProgressBar()
        if (0 != response.errorCode) {
            response.errorMsg?.let { loginView.registerFailed(it) }
            return
        }
        loginView.registerSuccess(response)
    }

    override fun registerFailed(errorMessage: String) {
        loginView.hideProgressBar()
        loginView.registerFailed(errorMessage)
    }

    override fun cancelRequest() {
        loginModel.cancelRequest()
    }

}