package com.example.carservice.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.carservice.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class ChooseCoordsFragment : Fragment() {

    private lateinit var mainMarker: Marker

    private val callback = OnMapReadyCallback { googleMap ->

        val ilia = LatLng(41.777143, 44.76093135)
        mainMarker = googleMap.addMarker(MarkerOptions().position(ilia).draggable(true))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ilia, 14f))

        googleMap.setOnCameraMoveListener {
            mainMarker.position = googleMap.cameraPosition.target
        }

        googleMap.setOnMarkerClickListener {
            (activity as AdminAddApartmentActivity).coordsChosen(it.position)
            return@setOnMarkerClickListener true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_choose_coords, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}