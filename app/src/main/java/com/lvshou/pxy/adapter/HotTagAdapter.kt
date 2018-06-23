package com.lvshou.pxy.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lvshou.pxy.R
import com.lvshou.pxy.bean.HotKeyResponse

/**
 * @desc：热门标签适配器
 * Created by JamesPxy on 2018/6/13 16:01
 */
class HotTagAdapter(val context: Context?, list: MutableList<HotKeyResponse.Data>)
    : BaseQuickAdapter<HotKeyResponse.Data, BaseViewHolder>(R.layout.item_hot_tag, list) {

    override fun convert(helper: BaseViewHolder, item: HotKeyResponse.Data?) {
        item ?: return
        helper.setText(R.id.tvTag, item.name)
        helper.addOnClickListener(R.id.tvTag)
    }
}