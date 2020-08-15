package com.example.carservice.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carservice.R
import com.example.carservice.models.Apartment
import com.example.carservice.ui.adapters.ApartmentsAdapter

class FavouritesFragment : Fragment() {

    companion object{
        const val TAG = "FAVOURITES_TAG"
    }

    private lateinit var apartmentsRecyclerView: RecyclerView
    private lateinit var apartmentsAdapter: ApartmentsAdapter
    private lateinit var noFavouritesTextView: TextView

    private var uiCreated = false

    private var favourites: ArrayList<Apartment> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI(view)

        uiCreated = true
        showBasedOnFavourites()
    }

    private fun initUI(view: View) {
        noFavouritesTextView = view.findViewById(R.id.no_favourites_text_view)
        initRecyclerView(view)
    }

    private fun initRecyclerView(view: View) {
        apartmentsRecyclerView = view.findViewById(R.id.favourites_recycler_view)

        apartmentsAdapter = ApartmentsAdapter()
        apartmentsRecyclerView.adapter = apartmentsAdapter
        apartmentsRecyclerView.layoutManager = LinearLayoutManager(view.context)
    }

    fun setApartments(apartments: ArrayList<Apartment>?) {
        apartments?.let {
            this.favourites = apartments
            showBasedOnFavourites()
        }
    }

    private fun showBasedOnFavourites() {
        if (uiCreated) {
            if (favourites.size == 0) {
                noFavouritesTextView.visibility = View.VISIBLE
            } else {
                apartmentsAdapter.setData(favourites)
                noFavouritesTextView.visibility = View.GONE
            }
        }
    }
}