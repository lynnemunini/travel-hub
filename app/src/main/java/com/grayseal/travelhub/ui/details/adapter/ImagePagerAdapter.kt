package com.grayseal.travelhub.ui.details.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grayseal.travelhub.R
import com.squareup.picasso.Picasso
import java.util.Locale

class ImagePagerAdapter(
    private var imageUrls: List<String>,
    private val backArrowClickListener: BackArrowClickListener
) :
    RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {
    private var isFavorite = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        holder.favouriteContainer.setOnClickListener {
            isFavorite = !isFavorite
            val drawableRes = if (isFavorite) R.drawable.round_favorite_24 else R.drawable.favorite
            holder.favourite.setImageResource(drawableRes)
        }
        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .fit()
            .centerCrop()
            .into(holder.imageView)
        holder.imageCountText.text = String.format(
            Locale.getDefault(),
            "%s/%s",
            "${position + 1}",
            "${imageUrls.size}"
        )
        holder.backContainer.setOnClickListener {
            backArrowClickListener.onBackArrowClicked()
        }
    }

    fun updateData(newData: List<String>) {
        imageUrls = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView =
            itemView.findViewById(R.id.image_view)
        val favourite: ImageView = itemView.findViewById(R.id.favourite)
        val favouriteContainer: View = itemView.findViewById(R.id.favourite_container)
        val back: ImageView = itemView.findViewById(R.id.back)
        val backContainer: View = itemView.findViewById(R.id.back_container)
        val imageCountText: TextView = itemView.findViewById(R.id.image_count_text)
    }

    interface BackArrowClickListener {
        fun onBackArrowClicked()
    }
}