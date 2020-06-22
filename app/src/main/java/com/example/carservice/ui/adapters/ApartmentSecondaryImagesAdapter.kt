package com.example.carservice.ui.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.carservice.R

class ApartmentSecondaryImagesAdapter :
    RecyclerView.Adapter<ApartmentSecondaryImagesAdapter.SecondaryImagesViewHolder>() {

    private var images = ArrayList<Bitmap>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SecondaryImagesViewHolder {
        return SecondaryImagesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.secondary_image_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: SecondaryImagesViewHolder, position: Int) {
        holder.setData(images[position])
    }

    fun setData(images : ArrayList<Bitmap>){
        this.images = images
    }

    class SecondaryImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var image : ImageView = itemView.findViewById(R.id.secondary_image_view)

        fun setData(bitmap: Bitmap){
            image.setImageBitmap(bitmap)
        }
    }
}