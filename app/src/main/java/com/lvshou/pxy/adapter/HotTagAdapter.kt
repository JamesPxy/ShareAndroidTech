package com.lvshou.pxy.adapter

import android.content.Context
import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lvshou.pxy.R
import com.lvshou.pxy.bean.HotKeyResponse
import getRandomColor

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
        } catch (_: Exception) {
        }
        helper.apply {
            setText(R.id.tvTag, item.name)
            addOnClickListener(R.id.tvTag)
            parseColor?.let { setTextColor(R.id.tvTag, it as Int) }
        }

    }
}