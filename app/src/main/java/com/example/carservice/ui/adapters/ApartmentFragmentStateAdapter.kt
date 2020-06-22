package com.example.carservice.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.carservice.ui.fragments.ApartmentFragment
import com.example.carservice.ui.fragments.ApartmentInfoFragment

class ApartmentFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return ApartmentFragment.NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ApartmentInfoFragment()
            else -> ApartmentInfoFragment()
        }
    }

}