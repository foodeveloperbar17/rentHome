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
import com.example.carservice.presenters.MainActivityPresenter
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker

class ApartmentInfoFragment : Fragment() {

    private lateinit var priceTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var userRatingsTextView: TextView

    private lateinit var rentButton: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_apartment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI(view)
        initUIActions()
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

    fun chooseDate() {
        val startDate = System.currentTimeMillis() - 1000 * 60 * 60 * 24
        val endDate = startDate + 1000 * 60 * 60 * 24 * 13
        val datePickerBuilder = MaterialDatePicker.Builder.dateRangePicker()
        datePickerBuilder.setCalendarConstraints(
            CalendarConstraints.Builder().setStart(startDate)
                .setEnd(endDate)
                .setValidator(object : CalendarConstraints.DateValidator {
                    override fun isValid(date: Long): Boolean {
                        return date in startDate until endDate
                    }
                    override fun writeToParcel(p0: Parcel?, p1: Int) {}

                    override fun describeContents(): Int { return 0 }
                }).build()
        )
        val datePicker = datePickerBuilder.build()
        datePicker.addOnPositiveButtonClickListener {
            Toast.makeText(
                this.context,
                "first is ${it.first} second is ${it.second}",
                Toast.LENGTH_LONG
            ).show()
        }
        fragmentManager?.let { fragmentManager ->
            datePicker.show(fragmentManager, "date picker")
        }
    }


}
