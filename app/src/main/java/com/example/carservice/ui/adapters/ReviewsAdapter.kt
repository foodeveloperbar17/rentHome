package com.example.carservice.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.carservice.R
import com.example.carservice.models.Review

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ReviewsHolder>() {

    private var reviews = ArrayList<Review>()

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

    fun setData(reviews: ArrayList<Review>) {
        this.reviews = reviews
        notifyDataSetChanged()
    }

    class ReviewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var reviewTextView: TextView = itemView.findViewById(R.id.review_item_text_view)
        private var dateTextView: TextView = itemView.findViewById(R.id.review_date_text_view)
        private var authorNameTextView: TextView =
            itemView.findViewById(R.id.review_author_text_view)
        private var reviewRatingBar: RatingBar = itemView.findViewById(R.id.review_item_rating_bar)


        fun setData(review: Review) {
            reviewTextView.text = review.userReview
            dateTextView.text = review.creationDate!!.toString()
            authorNameTextView.text = review.userName
            reviewRatingBar.rating = review.numStars.toFloat()
        }
    }
}