package com.lvshou.pxy.ui.activity

import android.content.Intent
import android.view.View
import com.lvshou.pxy.R
import com.lvshou.pxy.base.BaseActivity
import com.lvshou.pxy.bean.LoginResponse
import com.lvshou.pxy.constant.Constant
import com.lvshou.pxy.presenter.LoginPresenterImpl
import com.lvshou.pxy.utils.PreferenceUtils
import com.lvshou.pxy.view.LoginView
import kotlinx.android.synthetic.main.activity_login.*
import toast

class LoginAndRegisterActivity : BaseActivity(), LoginView, View.OnClickListener {

    private val loginPresenter: LoginPresenterImpl by lazy {
        LoginPresenterImpl(this)
    }
    /**
     * check login for SharedPreferences
     */
    private var isLogin: Boolean by PreferenceUtils(Constant.LOGIN_KEY, false)
    /**
     * local username
     */
    private var user: String by PreferenceUtils(Constant.USERNAME_KEY, "")
    /**
     * local password
     */
    private var pwd: String by PreferenceUtils(Constant.PASSWORD_KEY, "")

    private lateinit var userName: String
    private lateinit var password: String

    override fun init() {
        btn_login.setOnClickListener(this)
        btn_register.setOnClickListener(this)
        if (isLogin) {
            tvName.setText(user)
            tvPwd.setText(pwd)
//            startMainActivity()
        }
    }

    //不需要透明状态栏
    override fun initImmersionBar() {}

    override fun setLayoutId(): Int = R.layout.activity_login

    override fun onClick(view: View?) {
        when (view) {
            btn_login -> {
                if (checkIsInvalidedInfo()) {
                    loginPresenter.login(userName, password)
                }
            }
            btn_register -> {
                if (checkIsInvalidedInfo()) {
                    loginPresenter.register(userName, password)
                }
            }
        }
    }

    override fun cancelRequest() {
        loginPresenter.cancelRequest()
    }

    override fun loginSuccess(response: LoginResponse) {
        isLogin = true
        user = response.data.username
        pwd = response.data.password
        toast("登录成功")
        startMainActivity()
    }

    override fun loginFailed(errorMsg: String) {
        toast(errorMsg)
    }

    override fun registerSuccess(response: LoginResponse) {
        isLogin = true
        user = response.data.username
        pwd = response.data.password
        toast("注册成功")
        startMainActivity()
    }

    override fun registerFailed(errorMsg: String) {
        toast(errorMsg)
    }

    override fun showProgressBar() {
        login_progress.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        login_progress.visibility = View.GONE
    }

    override fun checkIsInvalidedInfo(): Boolean {
        userName = tvName.text.toString().trim()
        password = tvPwd.text.toString().trim()
        if (null == userName || userName.isEmpty()) {
            tvName.error = "用户名不能为空"
            return false
        }
        if (null == password || password.length < 6) {
            tvPwd.error = "密码至少输入六位"
            return false
        }
        return true
    }

    override fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}
