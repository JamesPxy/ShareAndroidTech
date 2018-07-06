package com.lvshou.pxy.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.flexbox.*
import com.lvshou.pxy.R
import com.lvshou.pxy.adapter.HotTagAdapter
import com.lvshou.pxy.base.BaseFragment
import com.lvshou.pxy.bean.HotKeyResponse
import com.lvshou.pxy.constant.Constant
import com.lvshou.pxy.presenter.HotTagPresenterImpl
import com.lvshou.pxy.ui.activity.ArticleDetailActivity
import com.lvshou.pxy.view.HotTagView
import kotlinx.android.synthetic.main.activity_tag.*
import loge
import toast

class TagFragment : BaseFragment(), HotTagView, BaseQuickAdapter.OnItemChildClickListener {

    private val presenter: HotTagPresenterImpl by lazy {/**/
        HotTagPresenterImpl(this)
    }

    /**
     * LinearLayoutManager
     */
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }

    private val hotTagDatas = mutableListOf<HotKeyResponse.Data>()

    private val adapter: HotTagAdapter by lazy {
        HotTagAdapter(activity, hotTagDatas)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //进行网络请求获取数据
        presenter.getHotTag()

        val flexBoxLayoutManager = FlexboxLayoutManager(activity)
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
            onItemChildClickListener = this@TagFragment
        }

    }

    override fun getHotTagSuccess(hotResult: HotKeyResponse) {
        hotResult.data?.let {
            //hotTagDatas.addAll(it)
            adapter.replaceData(it)
        }
    }

    override fun getHotTagFailed(errorMessage: String?) {
        errorMessage?.let {
            activity?.toast(it)
            loge("getHotTagFailed", it)
        }
    }

    override fun cancelRequest() {
        presenter.cancelRequest()
    }

    override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
//        activity?.toast("onclick: $position")
        Intent(activity, ArticleDetailActivity::class.java).run {
            putExtra(Constant.CONTENT_URL_KEY, hotTagDatas[position].link as String)
            putExtra(Constant.CONTENT_TITLE_KEY, hotTagDatas[position].name)
            startActivity(this)
        }
    }

}