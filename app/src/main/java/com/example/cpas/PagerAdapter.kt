package com.example.cpas

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(manager : FragmentManager) : FragmentPagerAdapter(manager) {
    private val fragmentList : ArrayList<Fragment> = ArrayList()
    private val titleList : ArrayList<String> = arrayListOf("취업 게시판", "일반 게시판")

    fun addFragment(fragment : Fragment) {
        fragmentList.add(fragment)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }
}