package com.lvshou.pxy.ui.activity

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.lvshou.pxy.MyPagerAdapter
import com.lvshou.pxy.R
import com.lvshou.pxy.TestKotlinFragment
import com.lvshou.pxy.dummy.DummyContent
import com.lvshou.pxy.dummy.ItemFragment
import com.lvshou.pxy.ui.fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener, ItemFragment.OnListFragmentInteractionListener {

    private val bottomIdList = intArrayOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                viewPager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                viewPager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                viewPager.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        var adapter = MyPagerAdapter(supportFragmentManager)
        val list = mutableListOf(HomeFragment(), TestKotlinFragment(), ItemFragment())
        adapter.setData(list)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 3
        viewPager.addOnPageChangeListener(this)
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        Log.i("onPageScrolled:", "pos=$position")
    }

    override fun onPageSelected(position: Int) {
        Log.i("onPageSelected:", "pos=$position")
        navigation.selectedItemId = bottomIdList[position]
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
    }

}
