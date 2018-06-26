package com.lvshou.pxy

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import com.lvshou.pxy.base.BaseFragment

/**
 * @desc：
 * Version:3.
 * Created by JamesPxy on 2018/6/11 14:39
 */
class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private lateinit var list: List<Fragment>

    fun setData(list: List<Fragment>) {
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