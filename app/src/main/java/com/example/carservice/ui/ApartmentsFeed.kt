package com.example.carservice.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.carservice.R
import com.example.carservice.model.Apartment

/**
 * A simple [Fragment] subclass.
 */
class ApartmentsFeed : Fragment() {

    private lateinit var apartmentsRecyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apartments_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI(view)
        setFeedDataToRecyclerView(view)
    }

    private fun initUI(view: View){
        apartmentsRecyclerView = view.findViewById(R.id.apartments_feed_recyclerview_id)
    }

    private fun setFeedDataToRecyclerView(view: View){
        val adapter = ApartmentsAdapter()
        val apartments = ArrayList<Apartment>()
        apartments.add(Apartment("kaloche", "kutaisshi 50 larad", 20.0, null, R.drawable.apart,0, 0.0))
        apartments.add(Apartment("kaloche", "kutaisshi 40 larad", 20.0, null, R.drawable.apart,0, 0.0))
        apartments.add(Apartment("kaloche", "kutaisshi 30 larad", 20.0, null, R.drawable.apart,0, 0.0))
        apartments.add(Apartment("kaloche", "kai xo 20 larad", 20.0, null, R.drawable.apart, 0, 0.0))
        adapter.setData(apartments)
        apartmentsRecyclerView.adapter = adapter
        apartmentsRecyclerView.layoutManager = LinearLayoutManager(view.context)
    }

    companion object{
        private var instance : ApartmentsFeed? = null

        fun getInstance(): ApartmentsFeed {
            if (instance == null){
                instance = ApartmentsFeed()
            }
            return instance!!
        }
    }


}
