package com.grayseal.travelhub.ui.details.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.grayseal.travelhub.data.model.TravelItem
import com.grayseal.travelhub.ui.main.view.MainDashboardActivity
import com.grayseal.travelhub.ui.main.viewmodel.EntriesViewModel

class DetailsActivity : AppCompatActivity() {
    private var entry: TravelItem? = null
    private val entriesViewModel: EntriesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun loadData() {
        val entryId = intent.getStringExtra(ENTRY_ID)
        entryId?.let { id ->
            entriesViewModel.getEntryById(id, applicationContext) { travelItem ->
                entry = travelItem
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@DetailsActivity, MainDashboardActivity::class.java))
        finish()

    }

    companion object {
        const val ENTRY_ID = "entry.id"
    }
}