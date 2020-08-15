package com.example.carservice.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import com.example.carservice.R
import com.example.carservice.db.FireDatabase
import com.example.carservice.models.Apartment
import com.example.carservice.models.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.model.ServerTimestamps
import java.util.*

class TempAddReviewFragment : Fragment() {
    companion object{
        const val TAG = "ADD_REVIEW_TAG"
    }

    private lateinit var apartment: Apartment

    private lateinit var reviewEditText: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_temp_add_review, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI(view)
        initUIActions()
    }

    private fun initUI(view: View) {
        reviewEditText = view.findViewById(R.id.review_edit_text)
        ratingBar = view.findViewById(R.id.apartment_review_rating_bar)
        submitButton = view.findViewById(R.id.submit_review_button)
    }

    private fun initUIActions() {
        submitButton.setOnClickListener {
            val reviewText = reviewEditText.text.toString()
            val rating = ratingBar.rating.toInt()
            val review = Review(
                UUID.randomUUID().toString(),
                reviewText,
                rating,
                FirebaseAuth.getInstance().currentUser?.displayName ?: "saxeli",
                null
            )
            FireDatabase.saveReview(apartment, review)
        }
    }

    fun setApartment(apartment: Apartment) {
        this.apartment = apartment
    }

}