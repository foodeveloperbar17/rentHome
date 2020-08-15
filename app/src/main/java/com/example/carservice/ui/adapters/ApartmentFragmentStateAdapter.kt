package com.example.carservice.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.carservice.db.FireDatabase
import com.example.carservice.models.Apartment
import com.example.carservice.ui.fragments.TempAddReviewFragment
import com.example.carservice.ui.fragments.apartmentFragments.ApartmentFragment
import com.example.carservice.ui.fragments.apartmentFragments.ApartmentInfoFragment
import com.example.carservice.ui.fragments.apartmentFragments.ApartmentReviewFragment
import com.example.carservice.ui.fragments.apartmentFragments.SmallMapFragment

class ApartmentFragmentStateAdapter(fragment: Fragment, private var apartment: Apartment) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return ApartmentFragment.NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val apartmentInfoFragment = ApartmentInfoFragment()
                apartmentInfoFragment.setApartment(apartment)
                apartmentInfoFragment
            }
            1 -> {
                val smallMapFragment = SmallMapFragment()
                smallMapFragment.setApartments(FireDatabase.getFetchedApartments())
                smallMapFragment.setMainApartment(apartment)
                smallMapFragment
            }
            2 -> {
                val addReviewFragment = TempAddReviewFragment()
                addReviewFragment.setApartment(apartment)
                addReviewFragment
            }
            3 -> {
                val apartmentReviewFragment = ApartmentReviewFragment()
                apartmentReviewFragment.setApartment(apartment)
                apartmentReviewFragment
            }
            else -> {
                val apartmentInfoFragment = ApartmentInfoFragment()
                apartmentInfoFragment.setApartment(apartment)
                apartmentInfoFragment
            }
        }
    }

}