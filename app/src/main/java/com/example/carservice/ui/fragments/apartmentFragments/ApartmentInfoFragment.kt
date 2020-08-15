package com.example.carservice.ui.fragments.apartmentFragments


import android.os.Bundle
import android.os.Parcel
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.example.carservice.R
import com.example.carservice.db.FireDatabase
import com.example.carservice.models.Apartment
import com.example.carservice.presenters.MainActivityPresenter
import com.google.android.material.datepicker.*

class ApartmentInfoFragment : Fragment() {
    companion object{
        const val TAG = "APARTMENT_INFO_TAG"
    }

    private lateinit var priceTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var userRatingsTextView: TextView

    private lateinit var rentButton: Button
    private lateinit var apartment: Apartment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_apartment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI(view)
        initUIActions()
        displayApartment()
    }

    fun setApartment(apartment: Apartment) {
        this.apartment = apartment
    }

    private fun initUI(view: View) {
        priceTextView = view.findViewById(R.id.price_text_view)
        descriptionTextView = view.findViewById(R.id.description_text_view)
        userRatingsTextView = view.findViewById(R.id.user_rating_text_view)

        rentButton = view.findViewById(R.id.rent_apartment_button)
    }

    private fun initUIActions() {
        rentButton.setOnClickListener {
            MainActivityPresenter.rentButtonClicked(this)
        }
    }

    private fun displayApartment() {
        apartment.let {
            priceTextView.text = "Price : ${it.price}"
            descriptionTextView.text = "Description : ${it.description}"
            userRatingsTextView.text = "Rating : ${it.userRating}"
        }
    }

    fun chooseDate() {
        val startDate = System.currentTimeMillis() - 1000 * 60 * 60 * 24
        val endDate = startDate + 1000 * 60 * 60 * 24 * 13
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
        datePickerBuilder.setCalendarConstraints(
            CalendarConstraints.Builder().setStart(startDate)
                .setEnd(endDate)
                .setValidator(object : CalendarConstraints.DateValidator {
                    override fun isValid(date: Long): Boolean {
                        return date in startDate until endDate && !apartment.rentHistory.contains(
                            date
                        )
                    }
                    override fun writeToParcel(p0: Parcel?, p1: Int) {}

                    override fun describeContents(): Int {
                        return 0
                    }
                }).build()
        )
        val datePicker = datePickerBuilder.build()
        datePicker.addOnPositiveButtonClickListener {
            MainActivityPresenter.dateChosen(it, apartment, FireDatabase.getCurrentUser()!!)
        }
        fragmentManager?.let { fragmentManager ->
            datePicker.show(fragmentManager, "date picker")
        }
    }


}
