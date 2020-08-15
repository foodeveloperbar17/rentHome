package com.example.carservice.ui.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.carservice.R
import com.example.carservice.models.Apartment
import com.example.carservice.models.User
import com.example.carservice.presenters.MainActivityPresenter
import com.example.carservice.ui.adapters.ApartmentsAdapter

class ApartmentsFeedFragment : Fragment() {
    private lateinit var apartmentsRecyclerView: RecyclerView
    private lateinit var apartmentsAdapter: ApartmentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_apartments_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI(view)
        MainActivityPresenter.onApartmentsFeedFragmentCreated(this)
        MainActivityPresenter.fetchData()
    }

    private fun initUI(view: View) {
        initRecyclerView(view)
    }

    private fun initRecyclerView(view: View) {
        apartmentsRecyclerView = view.findViewById(R.id.apartments_feed_recyclerview_id)

        apartmentsAdapter = ApartmentsAdapter()
        apartmentsRecyclerView.adapter = apartmentsAdapter
        apartmentsRecyclerView.layoutManager = LinearLayoutManager(view.context)
    }

    fun showApartments(apartments: List<Apartment>) {
        apartmentsAdapter.setData(apartments)
    }

    fun updateUIForUser(user: User) {
        val apartments = apartmentsAdapter.getData()
        apartments.forEach {
            it.isFavouriteForCurrentUser = user.favourites.contains(it.uuid)
        }
        apartmentsAdapter.setData(apartments)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        MainActivityPresenter.onApartmentsFeedFragmentDestroyed(this)
    }


}
