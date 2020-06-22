package com.example.carservice.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.carservice.R
import com.example.carservice.models.Apartment
import com.example.carservice.presenters.ApartmentsPresenter
import com.example.carservice.ui.adapters.ApartmentsAdapter

/**
 * A simple [Fragment] subclass.
 */
class ApartmentsFeedFragment : Fragment() {

    companion object{
        private var instance : ApartmentsFeedFragment? = null

        fun getInstance(): ApartmentsFeedFragment {
            if (instance == null){
                instance =
                    ApartmentsFeedFragment()
            }
            return instance!!
        }
    }
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
        ApartmentsPresenter.getInstance().onViewCreated(this)
        fetchData()

    }

    private fun initUI(view: View){
        initRecyclerView(view)
    }



    private fun initRecyclerView(view: View){
        apartmentsRecyclerView = view.findViewById(R.id.apartments_feed_recyclerview_id)

        apartmentsAdapter = ApartmentsAdapter()
        apartmentsRecyclerView.adapter = apartmentsAdapter
        apartmentsRecyclerView.layoutManager = LinearLayoutManager(view.context)
    }

    private fun setFeedDataToRecyclerView(view: View){
//        val apartments = ArrayList<Apartment>()
//        apartments.add(Apartment("kaloche", "kutaisshi 50 larad", 20.0, null, R.drawable.apart,0, 0.0))
//        apartments.add(Apartment("kaloche", "kutaisshi 40 larad", 20.0, null, R.drawable.apart,0, 0.0))
//        apartments.add(Apartment("kaloche", "kutaisshi 30 larad", 20.0, null, R.drawable.apart,0, 0.0))
//        apartments.add(Apartment("kaloche", "kai xo 20 larad", 20.0, null, R.drawable.apart, 0, 0.0))
//        adapter.setData(apartments)
    }

    private fun fetchData(){
        ApartmentsPresenter.getInstance().fetchData()
    }

    fun showApartments(apartments : List<Apartment>){
        (apartmentsRecyclerView.adapter as ApartmentsAdapter).setData(apartments)
    }


}
