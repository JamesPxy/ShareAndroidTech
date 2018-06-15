package com.lvshou.pxy.adapter

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lvshou.pxy.R
import com.lvshou.pxy.bean.BannerResponse

/**
 * @desc：
 * Created by JamesPxy on 2018/6/15 14:22
 */
class HomeBannerAdapter(val context: Context?, data: MutableList<BannerResponse.Data>) :
        BaseQuickAdapter<BannerResponse.Data, BaseViewHolder>(R.layout.banner_item, data) {

    override fun convert(helper: BaseViewHolder, item: BannerResponse.Data?) {
        item ?: return
        helper.setText(R.id.bannerTitle, item.title.trim())
        val imageView = helper.getView<ImageView>(R.id.bannerImage)
        Glide.with(context).load(item.imagePath).placeholder(R.mipmap.ic_launcher).into(imageView)
    }
}