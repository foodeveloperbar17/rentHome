package com.example.carservice.ui

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carservice.R
import com.example.carservice.model.Apartment

class ApartmentsAdapter : RecyclerView.Adapter<ApartmentsAdapter.ApartmentsHolder>() {

    private var apartments: List<Apartment> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApartmentsHolder {
        return ApartmentsHolder(LayoutInflater.from(parent.context).inflate(R.layout.apartment_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return apartments.size
    }

    override fun onBindViewHolder(holder: ApartmentsHolder, position: Int) {
        holder.setData(apartments[position])
    }

    fun setData(newAppartments: List<Apartment>){
        apartments = newAppartments
        notifyDataSetChanged()
    }

    inner class ApartmentsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var image : ImageView = itemView.findViewById(R.id.appartment_image_id)
        private var descriptionTextView: TextView = itemView.findViewById(R.id.appartment_description_textview_id)
        private var ratingBar: RatingBar = itemView.findViewById(R.id.appartment_rating_bar)

        fun setData(apartment: Apartment){
            if (apartment.description != null){
                descriptionTextView.text = apartment.description
            }
            if(apartment.imagePath != null){
                image.setImageResource(apartment.imagePath)
            }
            val layerDrawable = ratingBar.progressDrawable as LayerDrawable
            layerDrawable.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP) // this must be inside
//            layerDrawable.getDrawable(0).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP)
            layerDrawable.getDrawable(1).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP)

        }
    }
}