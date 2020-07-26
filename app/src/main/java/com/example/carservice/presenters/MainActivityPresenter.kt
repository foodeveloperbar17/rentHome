package com.example.carservice.presenters

import android.widget.CompoundButton
import android.widget.ToggleButton
import com.example.carservice.R
import com.example.carservice.db.FireDatabase
import com.example.carservice.models.Apartment
import com.example.carservice.models.Rent
import com.example.carservice.models.User
import com.example.carservice.ui.activities.MainActivity
import com.example.carservice.ui.fragments.ApartmentsFeedFragment
import com.example.carservice.ui.fragments.apartmentFragments.ApartmentInfoFragment
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

object MainActivityPresenter {
    private var mainActivity: MainActivity? = null
    private var apartmentsFeedFragment: ApartmentsFeedFragment? = null

    fun onActivityCreated(mainActivity: MainActivity) {
        this.mainActivity = mainActivity

        FirebaseAuth.getInstance().currentUser?.let {
            mainActivity.startLoading()
            FireDatabase.signInExistingUser(it.uid)
        }
    }

    fun onViewCreated(apartmentsFeedFragment: ApartmentsFeedFragment) {
        this.apartmentsFeedFragment = apartmentsFeedFragment
    }

    fun fetchData() {
        FireDatabase.fetchApartments()
    }

    fun apartmentsFetched(fetchedApartments: ArrayList<Apartment>) {
        this.apartmentsFeedFragment?.showApartments(fetchedApartments)
    }

    fun onFragmentDestroyed() {
        apartmentsFeedFragment = null
    }

    fun apartmentClicked(apartment: Apartment) {
        mainActivity?.apartmentChosen(apartment)
    }

    fun apartmentFavouriteClicked(
        apartment: Apartment,
        toggleButton: CompoundButton,
        favourite: Boolean
    ) {
        if (FirebaseAuth.getInstance().currentUser == null) {
            toggleButton.isChecked = false
            mainActivity?.promptUserSignIn()
        } else {
            if (favourite) {
                FireDatabase.addFavouriteApartment(apartment)
                mainActivity?.addCountToBadge()
            } else {
                FireDatabase.removeFavouriteApartment(apartment)
                mainActivity?.decreaseCountToBadge()
            }
        }
    }

    fun favouritesButtonClicked() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            mainActivity?.promptUserSignIn()
        } else {
            val favouritesCollection = FireDatabase.getCurrentUser()?.favourites?.values
            mainActivity?.showFavouritesFragment(ArrayList(favouritesCollection))
        }
    }

    fun profileButtonClicked() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            mainActivity?.promptUserSignIn()
        } else {
            mainActivity?.showProfileFragment(FireDatabase.getCurrentUser()!!)
        }
    }

    fun rentButtonClicked(apartmentInfoFragment: ApartmentInfoFragment) {
        if (FirebaseAuth.getInstance().currentUser == null) {
            mainActivity?.promptUserSignIn()
        } else {
            apartmentInfoFragment.chooseDate()
        }
    }

    fun dateChosen(date: Long, apartment: Apartment, user: User) {
        val rentDays = ArrayList<Long>()
        rentDays.add(date)
        val rent = Rent(
            UUID.randomUUID().toString(),
            user,
            apartment,
            rentDays,
            rentDays.size * apartment.price
        )
        apartment.rentHistory.addAll(rentDays)
        user.rentHistory.addAll(rentDays)
        FireDatabase.saveRent(rent, apartment, user)
    }

    fun successfullyRented() {
        mainActivity?.makeToast("successfully rented")
    }

    fun userFetchFinishedSuccessfully(status: String, user: User) {
        mainActivity?.signInFinishedSuccessfully(status, user)
    }

    fun userFetchFinishedFailed(status: String) {
        mainActivity?.signInFinished(status)
    }

    fun onActivityDestroyed() {
        mainActivity = null
    }

}