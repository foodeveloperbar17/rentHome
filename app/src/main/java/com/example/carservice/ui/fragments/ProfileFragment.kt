package com.example.carservice.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.carservice.R
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment() {

    companion object{
        private var instance : ProfileFragment? = null

        fun getInstance(): ProfileFragment {
            if (instance == null){
                instance =
                    ProfileFragment()
            }
            return instance!!
        }
    }

    private lateinit var profileImage : ImageView

    private lateinit var activeRentButton : MaterialButton
    private lateinit var cardsButton : MaterialButton
    private lateinit var rentHistoryButton : MaterialButton
    private lateinit var helpButton : MaterialButton
    private lateinit var termsAndConditionsButton : MaterialButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI(view)
//        persenter on view created
    }

    private fun initUI(view: View){
        profileImage = view.findViewById(R.id.profile_image_view)

        activeRentButton = view.findViewById(R.id.active_rent_button)
        cardsButton = view.findViewById(R.id.cards_button)
        rentHistoryButton = view.findViewById(R.id.rent_history_button)
        helpButton = view.findViewById(R.id.help_button)
        termsAndConditionsButton = view.findViewById(R.id.terms_and_conditions_button)
    }
}
