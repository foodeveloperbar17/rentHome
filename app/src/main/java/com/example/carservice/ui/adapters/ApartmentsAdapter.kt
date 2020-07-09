package com.example.carservice.ui.adapters

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.carservice.R
import com.example.carservice.models.Apartment
import com.example.carservice.presenters.MainActivityPresenter

class ApartmentsAdapter : RecyclerView.Adapter<ApartmentsAdapter.ApartmentsHolder>() {

    private var apartments: List<Apartment> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApartmentsHolder {
        return ApartmentsHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.apartment_item_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return apartments.size
    }

    override fun onBindViewHolder(holder: ApartmentsHolder, position: Int) {
        holder.setData(apartments[position])
    }

    fun setData(newApartments: List<Apartment>) {
        apartments = newApartments
        notifyDataSetChanged()
    }

    fun getData(): List<Apartment> {
        return apartments
    }

    inner class ApartmentsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var image: ImageView = itemView.findViewById(R.id.apartment_image_id)
        private var descriptionTextView: TextView =
            itemView.findViewById(R.id.apartment_description_textview_id)
        private var ratingBar: RatingBar = itemView.findViewById(R.id.apartment_rating_bar)
        private var rootView: View = itemView
        private var favouriteToggleButton: ToggleButton = itemView.findViewById(R.id.favourite_toggle_button)

        fun setData(apartment: Apartment) {
            apartment.description?.let { descriptionTextView.setText(apartment.description) }
            apartment.imagePath?.let { image.setImageResource(apartment.imagePath) }
            val layerDrawable = ratingBar.progressDrawable as LayerDrawable
            layerDrawable.getDrawable(2)
                .setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP) // this must be inside
//            layerDrawable.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP)
            layerDrawable.getDrawable(1).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP)

            rootView.setOnClickListener {
                MainActivityPresenter.apartmentClicked(apartment)
            }

            favouriteToggleButton.setOnCheckedChangeListener { toggleButton, b ->
                MainActivityPresenter.apartmentFavouriteClicked(apartment, toggleButton, b)
            }

            if(apartment.isFavouriteForCurrentUser){
                favouriteToggleButton.isChecked = true
            }

        }
    }
}