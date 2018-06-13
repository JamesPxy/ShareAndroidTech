package com.lvshou.pxy.ui.activity

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.flexbox.*
import com.lvshou.pxy.R
import com.lvshou.pxy.adapter.HotTagAdapter
import com.lvshou.pxy.base.BaseActivity
import com.lvshou.pxy.presenter.HotTagPresenterImpl
import com.lvshou.pxy.view.HotTagView
import kotlinx.android.synthetic.main.activity_tag.*
import loge
import toast
import top.jowanxu.wanandroidclient.bean.HotKeyResponse

/**
 * 热门搜索：http://www.wanandroid.com/hotkey/json
 *
 */
class TagActivity : BaseActivity(), HotTagView, BaseQuickAdapter.OnItemChildClickListener {
    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        toast("onclick: $position")
    }

    private val presenter: HotTagPresenterImpl by lazy {
        HotTagPresenterImpl(this)
    }

    /**
     * LinearLayoutManager
     */
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private val hotTagDatas = mutableListOf<HotKeyResponse.Data>()

    private val adapter: HotTagAdapter by lazy {
        HotTagAdapter(this, hotTagDatas)
    }

    override fun setLayoutId(): Int = R.layout.activity_tag

    override fun cancelRequest() {
        presenter.cancelRequest()
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar.titleBar(R.id.toolbar).init()
    }

    override fun init() {
        //进行网络请求获取数据
        presenter.getHotTag()

        val flexBoxLayoutManager = FlexboxLayoutManager(this)
        flexBoxLayoutManager.run {
            flexWrap = FlexWrap.WRAP  //按正常方向换行
            flexDirection = FlexDirection.ROW  //主轴为水平方向，起点在左端
            alignItems = AlignItems.CENTER  //定义项目在副轴轴上如何对齐
            justifyContent = JustifyContent.FLEX_START //多个轴对齐方式
        }
        recycleView.layoutManager = flexBoxLayoutManager

        adapter.run {
            bindToRecyclerView(recycleView)
            openLoadAnimation()
            onItemChildClickListener = this@TagActivity
        }
    }

    override fun getHotTagSuccess(hotResult: HotKeyResponse) {
        toast("getHotTagSuccess:\n${hotResult.toString()}")
        hotResult.data?.let {
            //hotTagDatas.addAll(it)
            adapter.replaceData(it)
        }
    }

    override fun getHotTagFailed(errorMessage: String?) {
        errorMessage?.let {
            toast(it)
            loge("getHotTagFailed", it)
        }
    }
}
