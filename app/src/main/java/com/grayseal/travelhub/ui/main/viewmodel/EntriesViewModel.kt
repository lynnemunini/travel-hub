package com.grayseal.travelhub.ui.main.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.grayseal.travelhub.R
import com.grayseal.travelhub.data.model.TravelItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class EntriesViewModel : ViewModel() {
    fun getAllEntries(context: Context, callback: (List<TravelItem>) -> Unit) {
        viewModelScope.launch {
            val entries = withContext(Dispatchers.IO) {
                val jsonFile = context.resources.openRawResource(R.raw.listings)
                val reader = InputStreamReader(jsonFile)

                val listType = object : TypeToken<List<TravelItem>>() {}.type
                Gson().fromJson<List<TravelItem>>(reader, listType)
            }
            callback(entries)
        }
    }

    fun getEntryById(id: String, context: Context, callback: (TravelItem?) -> Unit) {
        viewModelScope.launch {
            val entries = withContext(Dispatchers.IO) {
                val jsonFile = context.resources.openRawResource(R.raw.listings)
                val reader = InputStreamReader(jsonFile)

                val listType = object : TypeToken<List<TravelItem>>() {}.type
                Gson().fromJson<List<TravelItem>>(reader, listType)
            }

            // Find the entry with the specified id
            val entry = entries.find { it._id == id }

            callback(entry)
        }
    }
}