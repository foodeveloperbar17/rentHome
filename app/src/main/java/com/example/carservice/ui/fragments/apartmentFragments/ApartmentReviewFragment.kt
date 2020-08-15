package com.example.carservice.ui.fragments.apartmentFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.carservice.R
import com.example.carservice.models.Apartment
import com.example.carservice.models.Review
import com.example.carservice.presenters.MainActivityPresenter
import com.example.carservice.ui.adapters.ReviewsAdapter

class ApartmentReviewFragment : Fragment() {
    companion object{
        const val TAG = "APARTMENT_REVIEW_TAG"
    }

    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var reviewsAdapter: ReviewsAdapter
    private lateinit var apartment: Apartment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_apartment_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI(view)
        MainActivityPresenter.onReviewFragmentCreated(this)
        MainActivityPresenter.fetchApartmentReviews(apartment)
    }

    private fun initUI(view: View) {
        reviewsRecyclerView = view.findViewById(R.id.reviews_recycler_view)
        reviewsAdapter = ReviewsAdapter()
        reviewsRecyclerView.adapter = reviewsAdapter
        reviewsRecyclerView.layoutManager = LinearLayoutManager(view.context)
    }

    fun setApartment(apartment: Apartment) {
        this.apartment = apartment
    }

    fun reviewsFetchedForApartment(apartment: Apartment, reviews: ArrayList<Review>) {
        if (this.apartment == apartment) {
            reviewsAdapter.setData(reviews)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MainActivityPresenter.onApartmentReviewFragmentDestroyed(this)
    }
}