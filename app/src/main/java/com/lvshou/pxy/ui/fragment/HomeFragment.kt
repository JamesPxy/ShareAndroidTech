package com.lvshou.pxy.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.lvshou.pxy.R
import com.lvshou.pxy.adapter.HomeBannerAdapter
import com.lvshou.pxy.adapter.HomeListAdapter
import com.lvshou.pxy.base.BaseFragment
import com.lvshou.pxy.bean.BannerResponse
import com.lvshou.pxy.bean.Datas
import com.lvshou.pxy.bean.HomeListResponse
import com.lvshou.pxy.constant.Constant
import com.lvshou.pxy.presenter.HomePresenterImpl
import com.lvshou.pxy.ui.activity.LoginAndRegisterActivity
import com.lvshou.pxy.utils.HorizontalRecycleView
import com.lvshou.pxy.utils.PreferenceUtils
import com.lvshou.pxy.view.HomeFragmentView
import inflater
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import toast

/**
 * @desc：首页： 轮播组件和首页文章列表
 * Created by JamesPxy on 2018/6/15 12:15
 */
class HomeFragment : BaseFragment(), HomeFragmentView, SwipeRefreshLayout.OnRefreshListener {

    private lateinit var bannerRecyclerView: HorizontalRecycleView

    private val isLogin: Boolean by PreferenceUtils(Constant.LOGIN_KEY, false)

    private val presenter: HomePresenterImpl by lazy {
        HomePresenterImpl(this)
    }

    private val mBannerList = mutableListOf<BannerResponse.Data>()

    private val mBannerAdapter: HomeBannerAdapter by lazy {
        HomeBannerAdapter(activity, mBannerList)
    }

    private val mListData = mutableListOf<Datas>()

    private val mHomeAdapter: HomeListAdapter by lazy {
        HomeListAdapter(activity!!, mListData)
    }
    /**
     * Banner PagerSnapHelper
     */
    private val bannerPagerSnap: PagerSnapHelper by lazy {
        PagerSnapHelper()
    }

    //切换banner Job
    private var bannerSwitchJob: Job? = null

    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }

    //首页文章列表页数
    private var mCurrentPage = 0

    //轮播图pos
    private var mCurrentIndex = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bannerRecyclerView = activity?.inflater(R.layout.home_banner) as HorizontalRecycleView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //请求数据
        swipeRefreshLayout.run {
            isRefreshing = true
            setOnRefreshListener(this@HomeFragment)
        }
        presenter.getHomeBanner()
        presenter.getHomeList()

        recyclerView.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = mHomeAdapter
        }
        bannerRecyclerView.run {
            layoutManager = linearLayoutManager
            bannerPagerSnap.attachToRecyclerView(this)
            requestDisallowInterceptTouchEvent(true)
            setOnTouchListener(onTouchListener)
            addOnScrollListener(onScrollListener)
        }
        mBannerAdapter.run {
            bindToRecyclerView(bannerRecyclerView)
            onItemClickListener = this@HomeFragment.onBannerItemClickListener
        }
        mHomeAdapter.run {
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener(onRequestLoadMoreListener, recyclerView)
            onItemClickListener = this@HomeFragment.onItemClickListener
            onItemChildClickListener = this@HomeFragment.onItemChildClickListener
            addHeaderView(bannerRecyclerView)//添加首页banner
            setEmptyView(R.layout.fragment_home_empty)
        }

        floatingActionButton.setOnClickListener {
//            recyclerView.smoothScrollToPosition(0)
            recyclerView.scrollToPosition(0)
        }
    }

    override fun getHomeBannerSuccess(response: BannerResponse) {
        response.data?.let {
            mBannerAdapter.replaceData(it)
            startSwitchJob()
        }
    }

    override fun getHomeBannerFailed(errorMsg: String) {
        activity?.toast(errorMsg)
    }

    override fun getHomeListSuccess(response: HomeListResponse?) {
        response?.data?.datas?.let {
            mHomeAdapter.run {
                // 列表总数
                val total = response.data.total
                //当前页数
                mCurrentPage = response.data.curPage
                // 当前总数
                if (response.data.offset >= total || data.size >= total) {
                    loadMoreEnd()
                    return@let
                }
                if (swipeRefreshLayout.isRefreshing) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                loadMoreComplete()
                setEnableLoadMore(true)
            }
        }?.let {
            swipeRefreshLayout.isRefreshing = false
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun getHomeListFailed(errorMsg: String) {
        activity?.toast(errorMsg)
        swipeRefreshLayout.isRefreshing = false
    }


    override fun cancelRequest() {
        presenter.cancelRequest()
    }

    /**
     * ItemClickListener
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        //        todo  点击看文章详情
//        if (datas.size != 0) {
//            Intent(activity, ContentActivity::class.java).run {
//                putExtra(Constant.CONTENT_URL_KEY, datas[position].link)
//                putExtra(Constant.CONTENT_ID_KEY, datas[position].id)
//                putExtra(Constant.CONTENT_TITLE_KEY, datas[position].title)
//                startActivity(this)
//            }
//        }
    }
    private val onBannerItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        /*if (mBannerList.size != 0) {
            Intent(activity, ContentActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, bannerDatas[position].url)
                putExtra(Constant.CONTENT_TITLE_KEY, bannerDatas[position].title)
                startActivity(this)
            }
        }*/
    }
    /**
     * ItemChildClickListener
     */
    private val onItemChildClickListener =
            BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
                if (mListData.size != 0) {
                    val data = mListData[position]
                    when (view.id) {
                        R.id.homeItemType -> {
                            activity?.toast("待开发")
//                            data.chapterName ?: let {
//                                activity?.toast(getString(R.string.type_null))
//                                return@OnItemChildClickListener
//                            }
//                            Intent(activity, TypeContentActivity::class.java).run {
//                                putExtra(Constant.CONTENT_TARGET_KEY, true)
//                                putExtra(Constant.CONTENT_TITLE_KEY, data.chapterName)
//                                putExtra(Constant.CONTENT_CID_KEY, data.chapterId)
//                                startActivity(this)
//                            }
                        }
                        R.id.homeItemLike -> {
                            if (isLogin) {
                                val collect = data.collect
                                data.collect = !collect
                                mHomeAdapter.setData(position, data)
//                                TODO 收藏文章
//                                presenter.collectArticle(data.id, !collect)
                            } else {
                                Intent(activity, LoginAndRegisterActivity::class.java).run {
                                    startActivityForResult(this, Constant.MAIN_REQUEST_CODE)
                                }
                                activity?.toast("请先登录")
                            }
                        }
                    }
                }
            }
    /**
     * SCROLL_STATE_IDLE to start job
     */
    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    mCurrentIndex = linearLayoutManager.findFirstVisibleItemPosition()
                    startSwitchJob()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancelSwitchJob()
    }

    /**
     * ACTION_MOVE to cancel job
     */
    private val onTouchListener = View.OnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                cancelSwitchJob()
            }
        }
        false
    }

    /**
     * get Banner switch job
     */
    private fun getBannerSwitchJob() = launch {
        repeat(Int.MAX_VALUE) {
            if (mBannerList.size == 0) {
                return@launch
            }
            delay(BANNER_TIME)
            mCurrentIndex++
            val index = mCurrentIndex % mBannerList.size
            bannerRecyclerView.smoothScrollToPosition(index)
            mCurrentIndex = index
        }
    }

    /**
     * scroll to top
     */
//    fun smoothScrollToPosition() = recyclerView.scrollToPosition(0)

    /**
     * resume banner switch
     */
    private fun startSwitchJob() = bannerSwitchJob?.run {
        if (!isActive) {
            bannerSwitchJob = getBannerSwitchJob().apply { start() }
        }
    } ?: let {
        bannerSwitchJob = getBannerSwitchJob().apply { start() }
    }

    /**
     * cancel banner switch
     */
    private fun cancelSwitchJob() = bannerSwitchJob?.run {
        if (isActive) {
            cancel()
        }
    }

    /**
     * 刷新获取最新数据
     */
    override fun onRefresh() {
        swipeRefreshLayout.isRefreshing = true
        mHomeAdapter.setEnableLoadMore(false)
        cancelSwitchJob()
        presenter.getHomeBanner()
        mCurrentPage = 0
        presenter.getHomeList()//默认取第一页数据
    }

    /**
     * 加载更多
     */
    private val onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        presenter.getHomeList(mCurrentPage++)
    }

    companion object {
        private const val BANNER_TIME = 5000L
    }

}
