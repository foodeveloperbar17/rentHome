package com.example.carservice.db

import android.util.Log
import com.example.carservice.R
import com.example.carservice.models.Apartment
import com.example.carservice.models.User
import com.example.carservice.presenters.MainActivityPresenter
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object FireDatabase {

    private const val TAG = "luka"

    private val apartmentsRef = FirebaseFirestore.getInstance().collection("/apartments")
    private val usersRef = FirebaseFirestore.getInstance().collection("/users")

    private var currentUserRef: DocumentReference? = null
    private var currentUser: User? = null

    private var apartments: ArrayList<Apartment>? = null

    fun addApartment(apartment: Apartment) {
        val randomUUID = UUID.randomUUID().toString()
        apartment.uuid = randomUUID
        apartmentsRef.document(randomUUID).set(apartment)
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
//            DateValidatorPointBackward
            currentUser?.favourites?.let { favouritesMap ->
                fetchedApartments.forEach {
                    it.isFavouriteForCurrentUser = favouritesMap.containsKey(it.uuid)
                }
            }
            apartments = fetchedApartments
            MainActivityPresenter.apartmentsFetched(fetchedApartments)
        }
    }

    fun addFavouriteApartment(apartment: Apartment) {
        apartment.isFavouriteForCurrentUser = true
        currentUser!!.favourites ?: run {
            currentUser!!.favourites = HashMap()
        }
        currentUser!!.favourites!![apartment.uuid!!] = apartment
        currentUserRef!!.set(currentUser!!)
    }

    fun removeFavouriteApartment(apartment: Apartment) {
        currentUser!!.favourites!!.remove(apartment.uuid)
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
        }.addOnFailureListener { exception ->
            Log.d(TAG, "exception during init curr user $exception")
            MainActivityPresenter.userFetchFinishedFailed("unsuccessful")
        }
    }

    private fun initUserFromData(data: Map<String, Any>): User {
        val favourites: HashMap<String, Apartment>? = HashMap()
        val favouritesMap = data["favourites"] as HashMap<String, HashMap<String, String>>?
        favouritesMap?.forEach {
            val apartment = getApartmentFromData(it.key, it.value)
            apartment.isFavouriteForCurrentUser = true
            favourites!![it.key] = apartment
        }
        return User(
            data["uid"] as String,
            data["name"] as String,
            data["phoneNumber"] as String?,
            data["email"] as String?,
            data["pid"] as String?,
            favourites
        )
    }

    private fun getApartmentFromData(id: String, data: Map<String, Any>): Apartment {
        return Apartment(
            id,
            data["name"] as String?,
            data["description"] as String?,
            data["price"] as Double,
            LatLng(
                (data["location"] as Map<String, Double>).getOrElse("latitude") { 0.0 },
                (data["location"] as Map<String, Double>).getOrElse("longitude") { 0.0 }
            ),
            data["imagePath"] as String?,
            data["secondaryImagesPaths"] as ArrayList<String>
        )
    }

    fun getCurrentUser(): User? {
        return currentUser
    }


}