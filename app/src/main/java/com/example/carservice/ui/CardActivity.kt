package com.example.carservice.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import com.braintreepayments.cardform.view.CardForm
import com.example.carservice.R
import com.google.android.material.button.MaterialButton

class CardActivity : AppCompatActivity() {

    private lateinit var cardForm: CardForm
    private lateinit var addCardButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        initUI()
        initUIActions()
    }

    private fun initUI(){
        addCardButton = findViewById(R.id.add_card_button_id)
        cardForm = findViewById(R.id.card_form_id)

        cardForm.cardRequired(true)
            .expirationRequired(true)
            .cvvRequired(true)
            .postalCodeRequired(true)
            .mobileNumberRequired(true)
            .mobileNumberExplanation("SMS verification")
            .setup(this)
        cardForm.cvvEditText.inputType = InputType.TYPE_CLASS_NUMBER
    }

    private fun initUIActions(){
        addCardButton.setOnClickListener {
            if(cardForm.isValid){
                Toast.makeText(this, "valid", Toast.LENGTH_LONG).show()
            } else{
                Toast.makeText(this, "invalid", Toast.LENGTH_LONG).show()
            }
        }
    }
}
