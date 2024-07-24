package com.spin.wheel.chooser.wheeloffortune.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.spin.wheel.chooser.wheeloffortune.custom.PageFragment

class OnboardingAdapter(fragmentActivity: FragmentActivity, private val listPage: List<Int>) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return listPage.size
    }

    override fun createFragment(position: Int): Fragment {
        return PageFragment.newInstance(listPage[position])
    }
}