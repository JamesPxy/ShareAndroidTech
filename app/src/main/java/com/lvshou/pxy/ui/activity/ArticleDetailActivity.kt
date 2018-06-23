package com.lvshou.pxy.ui.activity

import android.content.Intent
import android.net.Uri
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.ChromeClientCallbackManager
import com.lvshou.pxy.R
import com.lvshou.pxy.base.BaseActivity
import com.lvshou.pxy.bean.HomeListResponse
import com.lvshou.pxy.constant.Constant
import com.lvshou.pxy.presenter.CollectArticlePresenterImpl
import com.lvshou.pxy.presenter.CollectOutsidePresenter
import com.lvshou.pxy.presenter.CollectOutsidePresenterImpl
import com.lvshou.pxy.utils.PreferenceUtils
import com.lvshou.pxy.view.CollectArticleView
import getAgentWeb
import kotlinx.android.synthetic.main.activity_article_detail.*
import toast

class ArticleDetailActivity : BaseActivity(),CollectArticleView {

    override fun collectArticleSuccess(result: HomeListResponse, isAdd: Boolean) {
        toast("收藏成功")
    }

    override fun collectArticleFailed(errorMessage: String?, isAdd: Boolean) {
        errorMessage?.let { toast(it) }
    }

    private lateinit var agentWeb: AgentWeb
    private lateinit var shareTitle: String
    private lateinit var shareUrl: String
    private var shareId: Int = 0

    private var isLogin: Boolean by PreferenceUtils(Constant.LOGIN_KEY, false)

    private val  presenter:CollectOutsidePresenterImpl by lazy {
        CollectOutsidePresenterImpl(this)
    }

    override fun initImmersionBar() {
        super.initImmersionBar()
        immersionBar?.titleBar(R.id.toolbar)?.init()
    }

    override fun init() {
        toolbar.run {
            title = getString(R.string.loading)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        intent.extras?.let {
            shareId = it.getInt(Constant.CONTENT_ID_KEY, 0)
            shareUrl = it.getString(Constant.CONTENT_URL_KEY)
            shareTitle = it.getString(Constant.CONTENT_TITLE_KEY)
            agentWeb = shareUrl.getAgentWeb(
                    this,
                    webContent,
                    LinearLayout.LayoutParams(-1, -1),
                    receivedTitleCallback
            )
        }
    }

    override fun setLayoutId(): Int = R.layout.activity_article_detail

    override fun cancelRequest() {
    }

    private val receivedTitleCallback = ChromeClientCallbackManager.ReceivedTitleCallback { _, title ->
        title?.let {
            toolbar.title = it
        }
    }

    override fun onPause() {
        agentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        agentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        agentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (agentWeb.handleKeyEvent(keyCode, event)) {
            true
        } else {
            finish()
            super.onKeyDown(keyCode, event)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_article_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menuShare -> {
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(
                            Intent.EXTRA_TEXT,
                            getString(
                                    R.string.share_article_url,
                                    getString(R.string.app_name),
                                    shareTitle,
                                    shareUrl
                            )
                    )
                    type = Constant.CONTENT_SHARE_TYPE
                    startActivity(Intent.createChooser(this, getString(R.string.share)))
                }
                return true
            }
            R.id.menuLike -> {
                if (!isLogin) {
                    Intent(this, LoginAndRegisterActivity::class.java).run {
                        startActivity(this)
                    }
                    toast(getString(R.string.login_hint))
                    return true
                }else{
                    presenter.collectOutSideArticle(
                            shareId,
                            shareTitle,
                            getString(R.string.outside_title),
                            shareUrl,
                            true)
                    return true
                }
            }
            R.id.menuBrowser -> {
                Intent().run {
                    action = "android.intent.action.VIEW"
                    data = Uri.parse(shareUrl)
                    startActivity(this)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
