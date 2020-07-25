package com.example.carservice.ui.fragments.apartmentFragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.carservice.R
import com.example.carservice.glide.GlideApp
import com.example.carservice.models.Apartment
import com.example.carservice.ui.adapters.ApartmentFragmentStateAdapter
import com.example.carservice.ui.adapters.ApartmentSecondaryImagesAdapter
import com.example.carservice.ui.adapters.GliderSecondaryImagesAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ApartmentFragment(private var apartment: Apartment) : Fragment() {

    companion object {
        const val NUM_TABS = 5

        private val tabNames = arrayOf(1, 2, 3, 4, 5)
    }

    private lateinit var mainImage: ImageView
    private lateinit var otherImagesRecyclerView: RecyclerView
    private lateinit var otherImagesAdapter: GliderSecondaryImagesAdapter

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
        displayModel(apartment)
    }

    private fun initUI(view: View) {
        mainImage = view.findViewById(R.id.apartment_main_image_view)

        initRecyclerView(view)

        tabLayout = view.findViewById(R.id.apartment_tab_layout)
        viewPager = view.findViewById(R.id.apartment_view_pager)

        viewPager.adapter = ApartmentFragmentStateAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "Tab ${tabNames[position]}"
        }.attach()
    }

    private fun initRecyclerView(view: View) {
        otherImagesRecyclerView = view.findViewById(R.id.other_images_recycler_view)
        otherImagesAdapter = GliderSecondaryImagesAdapter()
        otherImagesRecyclerView.adapter = otherImagesAdapter
        otherImagesRecyclerView.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun displayModel(apartment: Apartment) {
        apartment.imagePath?.let {
            GlideApp.with(this).load(createStorageReferenceFromString(it))
                .into(mainImage)
        }
        val storageReferenceArray = ArrayList(apartment.secondaryImagesPaths.map {
            createStorageReferenceFromString(it)
        })
        otherImagesAdapter.setData(storageReferenceArray)
    }

    private fun createStorageReferenceFromString(path: String): StorageReference {
        return FirebaseStorage.getInstance().reference.child(path)
    }
//    C:\Users\lukaa\OneDrive\Desktop\CarService\app\src\main\java\values
}