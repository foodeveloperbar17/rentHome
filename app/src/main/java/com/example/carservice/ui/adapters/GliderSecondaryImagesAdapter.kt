package com.example.carservice.ui.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.carservice.R
import com.example.carservice.glide.GlideApp
import com.google.firebase.storage.StorageReference

class GliderSecondaryImagesAdapter :
    RecyclerView.Adapter<GliderSecondaryImagesAdapter.GliderSecondaryImagesViewHolder>() {

    private var images = ArrayList<StorageReference>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GliderSecondaryImagesViewHolder {
        return GliderSecondaryImagesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.secondary_image_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: GliderSecondaryImagesViewHolder, position: Int) {
        holder.setData(images[position])
    }

    fun setData(images: ArrayList<StorageReference>) {
        this.images = images
        notifyDataSetChanged()
    }

    class GliderSecondaryImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var image: ImageView = itemView.findViewById(R.id.secondary_image_view)

        fun setData(storageReference: StorageReference) {
            GlideApp.with(image.context).load(storageReference).into(image)
        }
    }
}