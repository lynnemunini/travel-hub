package com.grayseal.travelhub.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
                    entryList.add(entry)
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

    inner class ViewHolder(itemView: View, entryClickedListener: OnEntryClickedListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val container: MaterialCardView
        val backgroundImage: ImageView
        private val entryClickedListener: OnEntryClickedListener

        init {
            container = itemView.findViewById(R.id.item_listing_container)
            backgroundImage = itemView.findViewById(R.id.background_image)
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