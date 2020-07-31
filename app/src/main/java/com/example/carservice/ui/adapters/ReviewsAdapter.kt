package com.example.carservice.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carservice.R

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ReviewsHolder>() {

    //    must be changed with array list of reviews
    private var reviews = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewsHolder {
        return ReviewsHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.review_item_layout, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    override fun onBindViewHolder(holder: ReviewsHolder, position: Int) {
        holder.setData(reviews[position])
    }

    class ReviewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var reviewTextView: TextView = itemView.findViewById(R.id.review_item_text_view)


        fun setData(review: String) {
            reviewTextView.text = review
        }
    }
}