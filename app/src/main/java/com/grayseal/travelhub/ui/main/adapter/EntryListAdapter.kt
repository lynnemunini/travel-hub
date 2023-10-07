package com.grayseal.travelhub.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.grayseal.travelhub.R
import com.grayseal.travelhub.data.model.TravelItem
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
                    ) || entry.location.city.lowercase(Locale.getDefault()).contains(
                        searchTerm.lowercase(Locale.getDefault())
                    ) || entry.location.street.lowercase(Locale.getDefault()).contains(
                        searchTerm.lowercase(Locale.getDefault())
                    ) || entry.location.country.lowercase(Locale.getDefault()).contains(
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
                            if (entry.id == item.id) {
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
        private val container: MaterialCardView
        private val entryClickedListener: OnEntryClickedListener

        init {
            container = itemView.findViewById(R.id.item_listing_container)
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