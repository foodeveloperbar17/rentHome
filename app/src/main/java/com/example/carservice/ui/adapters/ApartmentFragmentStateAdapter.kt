package com.example.carservice.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.carservice.db.FireDatabase
import com.example.carservice.presenters.MainActivityPresenter
import com.example.carservice.ui.fragments.apartmentFragments.ApartmentFragment
import com.example.carservice.ui.fragments.apartmentFragments.ApartmentInfoFragment
import com.example.carservice.ui.fragments.apartmentFragments.SmallMapFragment

class ApartmentFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return ApartmentFragment.NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ApartmentInfoFragment()
            1 -> {
                val smallMapFragment = SmallMapFragment()
                smallMapFragment.setApartments(FireDatabase.getFetchedApartments())
                smallMapFragment
            }
            else -> ApartmentInfoFragment()
        }
    }

}