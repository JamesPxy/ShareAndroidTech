package com.lvshou.pxy.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lvshou.pxy.R
import com.lvshou.pxy.view.BottomScrollView
import kotlinx.android.synthetic.main.activity_test_scroll_view.*

class TestScrollViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_scroll_view)

        scrollView.post {
            if (scrollView.height > scrollView.getChildAt(0).measuredHeight) {
                button.visibility = View.VISIBLE
            } else {
                scrollView.setOnScrollToBottomListener(object : BottomScrollView.OnScrollToBottomListener {
                    override fun onScrollToBottom(isBottom: Boolean) {
                        if (isBottom) {
                            button.visibility = View.VISIBLE
                        } else {
                            button.visibility = View.GONE
                        }
                    }
                }, button.measuredHeight)
            }
        }

    }
}
