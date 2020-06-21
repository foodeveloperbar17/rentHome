package com.example.carservice.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.carservice.R
import com.example.carservice.model.Apartment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class TempAddApartmentActivity : AppCompatActivity() {

    private lateinit var saveApartmentButton : Button

    private lateinit var nameTextView : EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var latTextView : EditText
    private lateinit var lngTextView: EditText

    private val apartmentsRef = FirebaseFirestore.getInstance().collection("/apartments")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temp_add_apartment)

        initUI()
        initUIActions()
    }

    private fun initUI(){
        saveApartmentButton = findViewById(R.id.add_apartment_button)

        nameTextView = findViewById(R.id.name_edit_text)
        descriptionEditText = findViewById(R.id.description_edit_text)
        priceEditText = findViewById(R.id.price_edit_text)
        latTextView = findViewById(R.id.lat_edit_text)
        lngTextView = findViewById(R.id.lng_edit_text)
    }

    private fun initUIActions(){
        saveApartmentButton.setOnClickListener {
            val name = nameTextView.text.toString()
            val description = descriptionEditText.text.toString()
            val price = priceEditText.text.toString().toDouble()
            val lat = latTextView.text.toString().toDouble()
            val lng = lngTextView.text.toString().toDouble()
            val apartment = Apartment(name, description, price, LatLng(lat, lng))
            apartmentsRef.document(UUID.randomUUID().toString()).set(apartment)
        }
    }
}
