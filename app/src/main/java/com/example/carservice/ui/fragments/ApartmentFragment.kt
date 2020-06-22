package com.example.carservice.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

import com.example.carservice.R
import com.example.carservice.ui.adapters.ApartmentFragmentStateAdapter
import com.example.carservice.ui.adapters.ApartmentSecondaryImagesAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ApartmentFragment : Fragment() {

    companion object {
        private var instance: ApartmentFragment? = null

        fun getInstance(): ApartmentFragment {
            if (instance == null) {
                instance = ApartmentFragment()
            }
            return instance!!
        }

        public val NUM_TABS = 5

//        to do: change with resources

        private val tabNames = arrayOf(1, 2, 3, 4, 5)
    }

    private lateinit var mainImage: ImageView
    private lateinit var otherImagesRecyclerView: RecyclerView
    private lateinit var otherImagesAdapter : ApartmentSecondaryImagesAdapter

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_apartment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI(view)
    }

    private fun initUI(view: View) {
        mainImage = view.findViewById(R.id.apartment_main_image_view)

        initRecyclerView(view)

        tabLayout = view.findViewById(R.id.apartment_tab_layout)
        viewPager = view.findViewById(R.id.apartment_view_pager)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Tab ${tabNames[position]}"
        }.attach()

        viewPager.adapter = ApartmentFragmentStateAdapter(this)
    }

    private fun initRecyclerView(view: View) {
        otherImagesRecyclerView = view.findViewById(R.id.other_images_recycler_view)
        otherImagesAdapter = ApartmentSecondaryImagesAdapter()
        otherImagesRecyclerView.adapter = otherImagesAdapter
        otherImagesRecyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
    }

//    to do: add set model




}
