package com.example.carservice.presenters

import com.example.carservice.R
import com.example.carservice.models.Apartment
import com.example.carservice.ui.fragments.ApartmentsFeedFragment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FirebaseFirestore

class ApartmentsPresenter{

    companion object  {
        var apartmentsPresenter: ApartmentsPresenter? = null

        fun getInstance(): ApartmentsPresenter {
            if(apartmentsPresenter == null){
                apartmentsPresenter = ApartmentsPresenter()
            }
            return apartmentsPresenter!!
        }
    }

    private val apartmentsCollectionRef = FirebaseFirestore.getInstance().collection("/apartments")

    private var apartmentsFeedFragment : ApartmentsFeedFragment? = null

    fun onViewCreated(apartmentsFeedFragment: ApartmentsFeedFragment){
        this.apartmentsFeedFragment = apartmentsFeedFragment
    }

    fun fetchData(){
        apartmentsCollectionRef.get().addOnSuccessListener {result ->
            var fetchedApartments = ArrayList<Apartment>()
            for(document in result){
                val data = document.data
                val apartment = Apartment(data["name"] as String?, data["description"] as String?, data["price"] as Double,
                    LatLng((data["location"] as Map<String, Double>)["latitude"] as Double, (data["location"] as Map<String, Double>)["longitude"] as Double),
                    R.drawable.apart)
                fetchedApartments.add(apartment)
            }
            apartmentsFeedFragment?.showApartments(fetchedApartments)
        }
    }
}