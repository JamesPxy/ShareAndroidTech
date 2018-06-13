package com.lvshou.pxy

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

/**
 * @desc：
 * Version:3.
 * Created by JamesPxy on 2018/6/11 14:39
 */
class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private lateinit var list: MutableList<Fragment>

    fun setData(list: MutableList<Fragment>) {
        this.list = list
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

//    override fun isViewFromObject(view: View, `object`: Any): Boolean {
//        return view==`object`
//    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return super.instantiateItem(container, position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
    }
}