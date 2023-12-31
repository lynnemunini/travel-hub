package com.grayseal.travelhub.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.grayseal.travelhub.R
import com.grayseal.travelhub.data.model.TravelItem
import com.grayseal.travelhub.ui.details.view.DetailsActivity
import com.grayseal.travelhub.ui.details.view.DetailsActivity.Companion.ENTRY_ID
import com.grayseal.travelhub.ui.main.adapter.EntryListAdapter
import com.grayseal.travelhub.ui.main.eventbus.SearchResultEvent
import com.grayseal.travelhub.ui.main.viewmodel.EntriesViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * The main dashboard activity of the TravelHub app.
 *
 * This activity displays a list of travel entries, allows searching, and handles click events
 * to view details of a selected entry.
 */
class MainDashboardActivity : AppCompatActivity(), EntryListAdapter.OnEntryClickedListener {
    private lateinit var entriesRecyclerView: RecyclerView
    private lateinit var entriesAdapter: EntryListAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var searchEditText: EditText
    private val entriesViewModel: EntriesViewModel by viewModels()
    private var entriesList: MutableList<TravelItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeResources()
        loadData()
    }

    override fun onStart() {
        super.onStart()
        try {
            EventBus.getDefault().register(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            EventBus.getDefault().unregister(this)

        } catch (ex: Exception) {
        }
    }

    private fun initializeResources() {
        entriesRecyclerView = findViewById(R.id.entries_recycler_view)
        progressBar = findViewById(R.id.list_loading_progress)
        searchEditText = findViewById(R.id.search_edit_text)
        entriesAdapter = EntryListAdapter(this@MainDashboardActivity, entriesList, this)
        entriesRecyclerView.adapter = entriesAdapter
        entriesRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (searchEditText.text.isNotEmpty()) entriesAdapter.search(searchEditText.text.toString()) else loadData()
            }
        })
    }

    private fun loadData() {
        progressBar.visibility = View.VISIBLE
        entriesViewModel.getAllEntries(applicationContext) { entries ->
            progressBar.visibility = View.GONE
            entriesList.clear()
            entriesList.addAll(entries.shuffled())
            entriesAdapter.notifyDataSetChanged()
            entriesAdapter.updateSearchableList(entriesList)
        }
    }

    /**
     * Handle the click event when a travel entry is clicked.
     *
     * @param position The position of the clicked entry in the list.
     */
    override fun onEntryClicked(position: Int) {
        val intent = Intent(this@MainDashboardActivity, DetailsActivity::class.java)
        intent.putExtra(ENTRY_ID, entriesList[position]._id)
        startActivity(intent)
        finish()
    }

    /**
     * Event handler for the empty search result event.
     *
     * @param event The search result event indicating whether the search result is empty.
     */
    @Subscribe
    fun emptySearchResultEvent(event: SearchResultEvent) {
        if (event.isEmpty) {
            entriesRecyclerView.visibility = View.GONE
        } else {
            entriesRecyclerView.visibility = View.VISIBLE
        }
    }

}