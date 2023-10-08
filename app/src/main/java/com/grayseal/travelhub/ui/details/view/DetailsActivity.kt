package com.grayseal.travelhub.ui.details.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.grayseal.travelhub.R
import com.grayseal.travelhub.data.model.TravelItem
import com.grayseal.travelhub.ui.details.adapter.ImagePagerAdapter
import com.grayseal.travelhub.ui.main.view.MainDashboardActivity
import com.grayseal.travelhub.ui.main.viewmodel.EntriesViewModel
import com.grayseal.travelhub.utils.ZoomOutPageTransformer

class DetailsActivity : AppCompatActivity() {
    private lateinit var imageViewPager: ViewPager2
    private lateinit var imageViewPagerAdapter: ImagePagerAdapter
    private var entry: TravelItem? = null
    private val entriesViewModel: EntriesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_layout)
        initializeResources()
        loadData()
    }

    private fun initializeResources() {
        imageViewPager = findViewById(R.id.image_view_pager)
        imageViewPagerAdapter = ImagePagerAdapter(emptyList())
        imageViewPager.adapter = imageViewPagerAdapter
        imageViewPager.setPageTransformer(ZoomOutPageTransformer())


        imageViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
    }

    private fun loadData() {
        val entryId = intent.getStringExtra(ENTRY_ID)
        entryId?.let { id ->
            entriesViewModel.getEntryById(id, applicationContext) { travelItem ->
                entry = travelItem
                imageViewPagerAdapter.updateData(entry?.photos ?: emptyList())
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