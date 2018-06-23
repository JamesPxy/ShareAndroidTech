package com.lvshou.pxy.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager
import com.gyf.barlibrary.ImmersionBar

/**
 * @desc：Activity 基类
 * Created by JamesPxy on 2018/6/13 10:43
 */
@SuppressLint("NewApi")
abstract class BaseActivity : AppCompatActivity() {

    protected var immersionBar: ImmersionBar? = null

    private val inputManger: InputMethodManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(setLayoutId())
        initImmersionBar()
        init()
    }

    abstract fun init()

    abstract fun setLayoutId(): Int

    protected open fun initImmersionBar() {
        immersionBar = ImmersionBar.with(this)
        immersionBar?.init()
    }

    /**
     * cancel request
     */
    protected abstract fun cancelRequest()

    override fun onDestroy() {
        super.onDestroy()
        immersionBar?.destroy()
        cancelRequest()
    }

    override fun finish() {
        super.finish()
        if (!isFinishing) {
            hideSoftKeyBoard()
        }
    }

    private fun hideSoftKeyBoard() {
        currentFocus.let {
            inputManger.hideSoftInputFromInputMethod(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
//          inputManger.hideSoftInputFromWindow(it.windowToken, 2)
        }
    }


}