package com.grayseal.travelhub.ui.main.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.grayseal.travelhub.R
import com.grayseal.travelhub.data.model.TravelItem
import com.grayseal.travelhub.ui.main.eventbus.SearchResultEvent
import com.squareup.picasso.Picasso
import org.greenrobot.eventbus.EventBus
import java.util.Locale

class EntryListAdapter(
    private val context: Context,
    private val entryList: MutableList<TravelItem>,
    private val onEntryClickedListener: OnEntryClickedListener
) : RecyclerView.Adapter<EntryListAdapter.ViewHolder>() {
    private val searchableCopy: MutableList<TravelItem> = ArrayList()
    private var isFavorite = false

    init {
        searchableCopy.addAll(entryList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_listing_view_layout, parent, false)
        return ViewHolder(view, onEntryClickedListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val travelItem = entryList[position]
        holder.uniqueTypeTextView.text = travelItem.uniqueType
        holder.nameTextView.text = toTitleCase(travelItem.name)
        holder.locationTextView.text = travelItem.location.name
        holder.ratingTextView.text = String.format(
            Locale.getDefault(),
            "%s %s",
            travelItem.rating,
            context.getString(R.string.rating)
        )
        holder.priceTextView.text = String.format(
            Locale.getDefault(),
            "%s %s",
            travelItem.price.currency,
            travelItem.price.amount
        )
        holder.favouriteContainer.setOnClickListener {
            isFavorite = !isFavorite
            val drawableRes = if (isFavorite) R.drawable.round_favorite_24 else R.drawable.favorite
            holder.favourite.setImageResource(drawableRes)
        }
        Picasso.get()
            .load(travelItem.photos[0])
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(holder.backgroundImage)
    }

    fun search(searchTerm: String) {
        // Clear the current entry list
        entryList.clear()

        if (searchTerm.isEmpty()) {
            entryList.addAll(searchableCopy)
        } else {
            for (entry in searchableCopy) {
                if (entry.location.name.lowercase(Locale.getDefault()).contains(
                        searchTerm.lowercase(Locale.getDefault())
                    )
                ) {
                    if (entryList.isEmpty()) {
                        // If the entry list is empty, directly add the matched entry
                        entryList.add(entry)
                    } else {
                        var alreadyExists = false
                        // Check if the entry already exists in the entry list
                        for (item in entryList) {
                            if (entry._id == item._id) {
                                Log.d("ENTRY", "${entryList}")
                                alreadyExists = true
                                break
                            }
                        }
                        // Add the entry to the list if it doesn't already exist
                        if (!alreadyExists) {
                            entryList.add(entry)
                        }
                    }
                }
            }
        }
        // Post an event to notify about the search result
        EventBus.getDefault().post(SearchResultEvent(entryList.isEmpty()))

        // Notify the adapter about the data change
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return entryList.size
    }

    fun updateSearchableList(items: List<TravelItem>) {
        searchableCopy.addAll(items)
    }

    private fun toTitleCase(input: String): String {
        val words = input.split(" ")
        val titleCaseWords = words.map { word ->
            word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()
        }
        return titleCaseWords.joinToString(" ") // Join the words back together with spaces
    }

    inner class ViewHolder(itemView: View, entryClickedListener: OnEntryClickedListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val container: MaterialCardView
        val backgroundImage: ImageView
        val uniqueTypeTextView: TextView
        val favourite: ImageView
        val favouriteContainer: View
        val locationTextView: TextView
        val ratingTextView: TextView
        val nameTextView: TextView
        val priceTextView: TextView
        private val entryClickedListener: OnEntryClickedListener

        init {
            container = itemView.findViewById(R.id.item_listing_container)
            backgroundImage = itemView.findViewById(R.id.background_image)
            uniqueTypeTextView = itemView.findViewById(R.id.unique_type)
            favourite = itemView.findViewById(R.id.favourite)
            favouriteContainer = itemView.findViewById(R.id.favourite_container)
            locationTextView = itemView.findViewById(R.id.location)
            ratingTextView = itemView.findViewById(R.id.rating)
            nameTextView = itemView.findViewById(R.id.name)
            priceTextView = itemView.findViewById(R.id.price)
            this.entryClickedListener = entryClickedListener
            container.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            this.entryClickedListener.onEntryClicked(adapterPosition)
        }
    }

    interface OnEntryClickedListener {
        fun onEntryClicked(position: Int)
    }
}