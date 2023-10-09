package com.grayseal.travelhub.ui.details.view

import android.content.Intent
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.grayseal.travelhub.R
import com.grayseal.travelhub.data.model.TravelItem
import com.grayseal.travelhub.ui.details.adapter.ImagePagerAdapter
import com.grayseal.travelhub.ui.main.view.MainDashboardActivity
import com.grayseal.travelhub.ui.main.viewmodel.EntriesViewModel
import com.grayseal.travelhub.utils.ZoomOutPageTransformer
import com.grayseal.travelhub.utils.toTitleCase
import java.util.Locale

class DetailsActivity : AppCompatActivity(), ImagePagerAdapter.BackArrowClickListener {
    private lateinit var imageViewPager: ViewPager2
    private lateinit var imageViewPagerAdapter: ImagePagerAdapter
    private lateinit var uniqueTypeTextView: TextView
    private lateinit var nameTextView: TextView
    private lateinit var locationTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var amenitiesTextView: TextView
    private lateinit var reviewsTextView: TextView
    private lateinit var bathTextView: TextView
    private lateinit var bedTextView: TextView
    private lateinit var bedroomTextView: TextView
    private lateinit var guestTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var loadingProgressBar: ProgressBar
    private var entry: TravelItem? = null
    private val entriesViewModel: EntriesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_layout)
        initializeResources()
        loadData()
    }

    private fun initializeResources() {
        loadingProgressBar = findViewById(R.id.loading_progress)
        uniqueTypeTextView = findViewById(R.id.unique_type)
        nameTextView = findViewById(R.id.name)
        locationTextView = findViewById(R.id.location)
        ratingTextView = findViewById(R.id.rating)
        amenitiesTextView = findViewById(R.id.amenities)
        reviewsTextView = findViewById(R.id.reviews)
        bathTextView = findViewById(R.id.bath)
        bedTextView = findViewById(R.id.bed)
        bedroomTextView = findViewById(R.id.bedroom)
        guestTextView = findViewById(R.id.guests)
        priceTextView = findViewById(R.id.price)
        imageViewPager = findViewById(R.id.image_view_pager)
        imageViewPagerAdapter = ImagePagerAdapter(emptyList(), this)
        imageViewPager.adapter = imageViewPagerAdapter
        imageViewPager.setPageTransformer(ZoomOutPageTransformer())

        reviewsTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        ratingTextView.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        amenitiesTextView.setTypeface(null, Typeface.ITALIC)
    }

    private fun loadData() {
        loadingProgressBar.visibility = View.VISIBLE
        val entryId = intent.getStringExtra(ENTRY_ID)
        entryId?.let { id ->
            entriesViewModel.getEntryById(id, applicationContext) { travelItem ->
                loadingProgressBar.visibility = View.GONE
                entry = travelItem
                imageViewPagerAdapter.updateData(entry?.photos ?: emptyList())
                uniqueTypeTextView.text = toTitleCase(entry?.uniqueType.toString())
                nameTextView.text = toTitleCase(entry?.name.toString())
                locationTextView.text = toTitleCase(entry?.location?.name.toString())
                ratingTextView.text = String.format(
                    Locale.getDefault(),
                    "%s %s",
                    travelItem?.rating,
                    getString(R.string.rating)
                )
                reviewsTextView.text = String.format(
                    Locale.getDefault(),
                    "%s %s",
                    travelItem?.reviews?.size,
                    getString(R.string.reviews)
                )
                priceTextView.text = String.format(
                    Locale.getDefault(),
                    "%s %s",
                    travelItem?.price?.currency,
                    travelItem?.price?.amount
                )
                amenitiesTextView.text = String.format(
                    Locale.getDefault(),
                    "%s: %s",
                    getString(R.string.amenities),
                    travelItem?.amenities?.joinToString(", ")
                )
                bedTextView.text = String.format(
                    Locale.getDefault(),
                    "%s %s",
                    travelItem?.details?.beds,
                    "bed"
                )
                bathTextView.text = String.format(
                    Locale.getDefault(),
                    "%s %s",
                    travelItem?.details?.bath,
                    "bathroom"
                )
                bedroomTextView.text = String.format(
                    Locale.getDefault(),
                    "%s %s",
                    travelItem?.details?.bedroom,
                    "bedroom"
                )
                guestTextView.text = String.format(
                    Locale.getDefault(),
                    "%s %s",
                    travelItem?.details?.beds,
                    "guest"
                )
            }
        }
    }

    override fun onBackArrowClicked() {
        onBackPressed()
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