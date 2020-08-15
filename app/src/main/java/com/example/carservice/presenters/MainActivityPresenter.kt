package com.example.carservice.presenters

import android.widget.CompoundButton
import android.widget.ToggleButton
import com.example.carservice.R
import com.example.carservice.db.FireDatabase
import com.example.carservice.models.*
import com.example.carservice.ui.activities.MainActivity
import com.example.carservice.ui.fragments.ApartmentsFeedFragment
import com.example.carservice.ui.fragments.apartmentFragments.ApartmentInfoFragment
import com.example.carservice.ui.fragments.apartmentFragments.ApartmentReviewFragment
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import kotlin.collections.ArrayList

object MainActivityPresenter {
    private var mainActivity: MainActivity? = null
    private var apartmentsFeedFragment: ApartmentsFeedFragment? = null
    private var reviewFragment: ApartmentReviewFragment? = null

    fun onActivityCreated(mainActivity: MainActivity) {
        this.mainActivity = mainActivity

        FirebaseAuth.getInstance().currentUser?.let {
            mainActivity.startLoading()
            FireDatabase.signInExistingUser(it.uid)
        }
    }

    fun onApartmentsFeedFragmentCreated(apartmentsFeedFragment: ApartmentsFeedFragment) {
        this.apartmentsFeedFragment = apartmentsFeedFragment
    }

    fun onReviewFragmentCreated(reviewFragment: ApartmentReviewFragment) {
        this.reviewFragment = reviewFragment
    }

    fun fetchData() {
        FireDatabase.fetchApartments()
    }

    fun apartmentsFetched(fetchedApartments: ArrayList<Apartment>) {
        this.apartmentsFeedFragment?.showApartments(fetchedApartments)
    }

    fun fetchApartmentReviews(apartment: Apartment) {
        FireDatabase.fetchApartmentReviews(apartment)
    }

    fun reviewsFetched(apartment: Apartment, reviews: ArrayList<Review>) {
        reviewFragment?.reviewsFetchedForApartment(apartment, reviews)
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
            val favouritesIdsCollection = FireDatabase.getCurrentUser()?.favourites
            val allApartments = FireDatabase.getFetchedApartments()
            var favouriteApartments = ArrayList<Apartment>()
            favouritesIdsCollection?.let { collection ->
                favouriteApartments = ArrayList(allApartments.filter { apartment ->
                    collection.contains(apartment.uuid)
                })
            }
            mainActivity?.showFavouritesFragment(ArrayList(favouriteApartments))
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
        val moneySpent = rentDays.size * apartment.price
        val rent = Rent(
            UUID.randomUUID().toString(),
            user,
            apartment,
            rentDays,
            moneySpent
        )
        apartment.rentHistory.addAll(rentDays)
        val rentHistoryModel = UserRentHistoryModel(
            UUID.randomUUID().toString(),
            user.uid,
            apartment.uuid,
            apartment.name.orEmpty(),
            apartment.description.orEmpty(),
            moneySpent,
            rentDays.size,
            rentDays
        )
        user.rentHistory ?: kotlin.run {
            user.rentHistory = ArrayList<UserRentHistoryModel>()
        }
        user.rentHistory!!.add(rentHistoryModel)
        FireDatabase.saveRent(rent, apartment, user, rentHistoryModel)
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

    fun onApartmentReviewFragmentDestroyed(apartmentReviewFragment: ApartmentReviewFragment) {
        if (reviewFragment == apartmentReviewFragment) {
            reviewFragment = null
        }
    }

    fun onApartmentsFeedFragmentDestroyed(apartmentsFeedFragment: ApartmentsFeedFragment) {
        if (this.apartmentsFeedFragment == apartmentsFeedFragment) {
            this.apartmentsFeedFragment = null
        }
    }

    fun onActivityDestroyed() {
        mainActivity = null
    }

}