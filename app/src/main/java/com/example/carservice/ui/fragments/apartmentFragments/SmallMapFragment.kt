package com.example.carservice.ui.fragments.apartmentFragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.carservice.R
import com.example.carservice.models.Apartment
import com.example.carservice.presenters.MainActivityPresenter

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class SmallMapFragment(private var apartments: ArrayList<Apartment>) : Fragment(),
    GoogleMap.OnMarkerClickListener {

    private lateinit var googleMap: GoogleMap
    private val markerApartmentMap = HashMap<Marker, Apartment>()

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val tbilisi = LatLng(41.73, 44.77)
        this.googleMap = googleMap
//        googleMap.addMarker(MarkerOptions().position(tbilisi).title("Marker in Tbilisi"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(tbilisi))

        googleMap.setOnMarkerClickListener(this)

        addApartmentsAsMarkers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_small_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun addApartmentsAsMarkers() {
        apartments.forEach { apartment ->
            apartment.location?.let {
                val marker = googleMap.addMarker(MarkerOptions().position(it).title(apartment.name))
                markerApartmentMap.put(marker, apartment)
            }
        }
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        val apartment = markerApartmentMap[marker]
        apartment.let {
            MainActivityPresenter.apartmentClicked(it!!)
        }
        return true
    }
}