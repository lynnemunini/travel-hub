package com.grayseal.travelhub.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.grayseal.travelhub.R
import com.grayseal.travelhub.data.model.TravelItem
import com.grayseal.travelhub.ui.main.viewmodel.EntriesViewModel

class MainDashboardActivity : AppCompatActivity() {
    private lateinit var entriesRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var searchEditText: EditText
    private val entriesViewModel: EntriesViewModel by viewModels()
    private var entries: MutableList<TravelItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeResources()
        loadData()
    }

    private fun initializeResources() {
        entriesRecyclerView = findViewById(R.id.entries_recycler_view)
        progressBar = findViewById(R.id.list_loading_progress)
        searchEditText = findViewById(R.id.search_edit_text)
    }

    private fun loadData() {
        progressBar.visibility = View.VISIBLE
        entriesViewModel.getAllEntries(applicationContext) { entries ->
            // Handle the retrieved entries here (e.g., update UI or pass data to your adapter)
            Log.d("SIZE", "${entries.size}")
        }
    }
}