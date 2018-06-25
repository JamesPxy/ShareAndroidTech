package com.lvshou.pxy.adapter

import android.content.Context
import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lvshou.pxy.R
import com.lvshou.pxy.bean.HotKeyResponse
import getRandomColor
import loge

/**
 * @desc：热门标签适配器
 * Created by JamesPxy on 2018/6/13 16:01
 */
class HotTagAdapter(val context: Context?, list: MutableList<HotKeyResponse.Data>)
    : BaseQuickAdapter<HotKeyResponse.Data, BaseViewHolder>(R.layout.item_hot_tag, list) {

    override fun convert(helper: BaseViewHolder, item: HotKeyResponse.Data?) {
        item ?: return

        val parseColor = try {
            Color.parseColor(getRandomColor())
        } catch (e: Exception) {
            loge("HotTagAdapter exception:",e.localizedMessage)
            @Suppress("DEPRECATION")
            context?.resources?.getColor(R.color.black)
        }
        helper.apply {
            setText(R.id.tvTag, item.name)
            addOnClickListener(R.id.tvTag)
            if (null != parseColor) {
                setTextColor(R.id.tvTag, parseColor)
            }
        }

    }
}