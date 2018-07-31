package com.lvshou.pxy.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.ScrollView

/**
 * 监听scrollView是否滚动到底部
 *
 * @desc： Created by JamesPxy on 2018/7/27 17:29
 */
class BottomScrollView : ScrollView {

    private var mListener: OnScrollToBottomListener? = null
    private var maxScrollY = -1
    private var margin = 100
    private var hasToBottom: Boolean = false
    private var currentStatus: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean,
                                clampedY: Boolean) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
       /* if (scrollY != 0 && null != mListener) {
            mListener?.onScrollToBottom(clampedY)
            hasToBottom = clampedY
        }*/
        if (-1 == maxScrollY) {
            maxScrollY = getChildAt(0).measuredHeight - height
        }

        currentStatus = maxScrollY <= scrollY + margin
        //状态改变时才发起回调
        if (hasToBottom != currentStatus) {
            hasToBottom = currentStatus
            mListener?.onScrollToBottom(hasToBottom)
        }

    }

    fun setOnScrollToBottomListener(listener: OnScrollToBottomListener) {
        mListener = listener
    }

    fun setOnScrollToBottomListener(listener: OnScrollToBottomListener, margin: Int) {
        mListener = listener
        this.margin = margin
    }

    interface OnScrollToBottomListener {
        fun onScrollToBottom(isBottom: Boolean)
    }

}