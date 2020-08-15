package com.example.carservice.db

import android.util.Log
import com.example.carservice.R
import com.example.carservice.models.*
import com.example.carservice.presenters.MainActivityPresenter
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.maps.model.PlaceDetails
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object FireDatabase {

    private const val TAG = "luka"

    private val apartmentsRef = FirebaseFirestore.getInstance().collection("/apartments")
    private val usersRef = FirebaseFirestore.getInstance().collection("/users")
    private val rentsRef = FirebaseFirestore.getInstance().collection("/rents")

    private var currentUserRef: DocumentReference? = null
    private var currentUser: User? = null

    private var apartments: ArrayList<Apartment>? = null

    fun addApartment(apartment: Apartment) {
        apartmentsRef.document(apartment.uuid).set(apartment)
    }

    fun fetchApartments() {
        if (apartments == null) {
            fetchApartmentsFromFirebase()
        } else {
            MainActivityPresenter.apartmentsFetched(apartments!!)
        }
    }

    fun getFetchedApartments(): ArrayList<Apartment> {
        return apartments!!
    }

    private fun fetchApartmentsFromFirebase() {
        apartmentsRef.get().addOnSuccessListener { result ->
            val fetchedApartments = ArrayList<Apartment>()
            for (document in result) {
                val apartment = getApartmentFromData(document.id, document.data)
                fetchedApartments.add(apartment)
            }

            fetchedApartments.forEach {
                it.isFavouriteForCurrentUser = currentUser?.favourites?.contains(it.uuid) ?: false
            }
            apartments = fetchedApartments
            MainActivityPresenter.apartmentsFetched(fetchedApartments)
        }
    }

    fun fetchApartmentReviews(apartment: Apartment) {
        val reviewsRef = apartmentsRef.document(apartment.uuid).collection("reviews")
        reviewsRef.get().addOnSuccessListener {
            val fetchedReviews = ArrayList<Review>()
            it.forEach { document ->
                fetchedReviews.add(getReviewFromData(document.data))
            }
            MainActivityPresenter.reviewsFetched(apartment, fetchedReviews)
        }
    }

    fun addFavouriteApartment(apartment: Apartment) {
        apartment.isFavouriteForCurrentUser = true
        if (!currentUser!!.favourites.contains(apartment.uuid)) {
            currentUser!!.favourites.add(apartment.uuid)
            currentUserRef!!.set(currentUser!!)
        }
    }

    fun removeFavouriteApartment(apartment: Apartment) {
        apartment.isFavouriteForCurrentUser = false
        currentUser!!.favourites.remove(apartment.uuid)
        currentUserRef!!.set(currentUser!!)
    }


    private fun signInNewUser(user: User) {
        currentUser = user
        currentUserRef = usersRef.document(user.uid)
        currentUserRef!!.set(user)
    }

    fun signInExistingUser(uid: String) {
        currentUserRef = usersRef.document(uid)
        fetchCurrentUser()
    }

    private fun fetchCurrentUser() {
        currentUserRef!!.get().addOnSuccessListener { document ->
            if (document != null && document.data != null) {
                currentUser = initUserFromData(document.data!!)
                MainActivityPresenter.userFetchFinishedSuccessfully("successfully", currentUser!!)
            } else {
                FirebaseAuth.getInstance().signOut()
                MainActivityPresenter.userFetchFinishedFailed("sign in failed")
            }
        }.addOnFailureListener { exception ->
            Log.d(TAG, "exception: ", exception)
            MainActivityPresenter.userFetchFinishedFailed("unsuccessful")
        }
    }

    fun createOrFetchUser(user: User) {
        currentUserRef = usersRef.document(user.uid)
        currentUserRef!!.get().addOnSuccessListener { document ->
            if (document != null && document.data != null) {
                currentUser = initUserFromData(document.data!!)
            } else {
                signInNewUser(user)
            }
            MainActivityPresenter.userFetchFinishedSuccessfully("successfully", currentUser!!)
        }.addOnFailureListener {
            MainActivityPresenter.userFetchFinishedFailed("unsuccessful")
        }
    }

    private fun initUserFromData(data: Map<String, Any>): User {
        val favouritesList = data["favourites"] as ArrayList<String>?
        val favourites = favouritesList?.let { ArrayList(it) } ?: kotlin.run { ArrayList<String>() }

        return User(
            data["uid"] as String,
            data["name"] as String,
            data["phoneNumber"] as String?,
            data["email"] as String?,
            data["pid"] as String?,
            favourites,
            data["userRole"] as String,
            null
        )
    }

    private fun getApartmentFromData(id: String, data: Map<String, Any>): Apartment {
        return Apartment(
            id,
            data["name"] as String,
            data["description"] as String?,
            data["price"] as Double,
            LatLng(
                (data["location"] as Map<String, Double>).getOrElse("latitude") { 0.0 },
                (data["location"] as Map<String, Double>).getOrElse("longitude") { 0.0 }
            ),
            data["imagesPaths"] as ArrayList<String>,
            data["numReviews"] as Long,
            data["numRented"] as Long,
            (data["userRating"] as Number).toDouble(),
            (data["overAllRating"] as Number).toDouble(),
            data["rentHistory"]?.let {
                it as ArrayList<Long>
            } ?: kotlin.run { ArrayList<Long>() }
        )
    }

    private fun getReviewFromData(data: Map<String, Any>): Review {
        return Review(
            data["uid"] as String,
            data["userReview"] as String,
            (data["numStars"] as Long).toInt(),
            data["userName"] as String,
            (data["creationDate"] as Timestamp).toDate()
        )
    }

    fun saveRent(
        rent: Rent,
        apartment: Apartment,
        user: User,
        userRentHistoryModel: UserRentHistoryModel
    ) {
        val rentRef = rentsRef.document(rent.uid)
        val apartmentRef = apartmentsRef.document(apartment.uuid)
        val userRef = usersRef.document(user.uid)
        val userRentHistoryRef =
            userRef.collection("rentHistory").document(userRentHistoryModel.uid)
        FirebaseFirestore.getInstance().runBatch {
            it.set(rentRef, rent)
            it.set(apartmentRef, apartment)
//            it.set(userRef, user)
            it.set(userRentHistoryRef, userRentHistoryModel)
        }.addOnCompleteListener {
            MainActivityPresenter.successfullyRented()
        }
    }

    fun saveReview(apartment: Apartment, review: Review) {
        val reviewRef = apartmentsRef.document(apartment.uuid)
            .collection("reviews").document(review.uid)
        reviewRef.set(review)
    }

    fun getCurrentUser(): User? {
        return currentUser
    }

    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        currentUser = null
        currentUserRef = null
    }
}