package com.grayseal.travelhub.ui.details.view

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.grayseal.travelhub.R
import com.grayseal.travelhub.data.model.TravelItem
import com.grayseal.travelhub.ui.details.adapter.ImagePagerAdapter
import com.grayseal.travelhub.ui.main.view.MainDashboardActivity
import com.grayseal.travelhub.ui.main.viewmodel.EntriesViewModel
import com.grayseal.travelhub.utils.CalendarDecorator
import com.grayseal.travelhub.utils.ZoomOutPageTransformer
import com.grayseal.travelhub.utils.toTitleCase
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.Locale

/**
 * Activity for displaying details of a travel item, including images, information, and a map.
 */
class DetailsActivity : AppCompatActivity(), ImagePagerAdapter.BackArrowClickListener,
    OnMapReadyCallback {
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
    private lateinit var descriptionTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var locationAddress: TextView
    private lateinit var cancellationPolicy: TextView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var myMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private var entry: TravelItem? = null
    private val entriesViewModel: EntriesViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_layout)
        initializeResources()
        loadData()

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
    }

    private fun initializeResources() {
        loadingProgressBar = findViewById(R.id.loading_progress)
        uniqueTypeTextView = findViewById(R.id.unique_type)
        calendarView = findViewById(R.id.calendarView)
        nameTextView = findViewById(R.id.name)
        locationTextView = findViewById(R.id.location)
        ratingTextView = findViewById(R.id.rating)
        amenitiesTextView = findViewById(R.id.amenities)
        cancellationPolicy = findViewById(R.id.cancellation_policy)
        locationAddress = findViewById(R.id.location_name)
        reviewsTextView = findViewById(R.id.reviews)
        descriptionTextView = findViewById(R.id.description)
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
        cancellationPolicy.setTypeface(null, Typeface.ITALIC)
    }

    private fun loadData() {
        // Load data for the travel item and populate UI elements.
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
                descriptionTextView.text = String.format(
                    Locale.getDefault(),
                    "%s: %s",
                    "Description",
                    travelItem?.description
                )
                bedTextView.text = String.format(
                    Locale.getDefault(),
                    "%s %s",
                    travelItem?.details?.beds,
                    "Bed"
                )
                bathTextView.text = String.format(
                    Locale.getDefault(),
                    "%s %s",
                    travelItem?.details?.bath,
                    "Bathroom"
                )
                bedroomTextView.text = String.format(
                    Locale.getDefault(),
                    "%s %s",
                    travelItem?.details?.bedroom,
                    "Bedroom"
                )
                guestTextView.text = String.format(
                    Locale.getDefault(),
                    "%s %s",
                    travelItem?.details?.beds,
                    "Guest"
                )
                locationAddress.text = toTitleCase(entry?.location?.name.toString())
                cancellationPolicy.text = String.format(
                    Locale.getDefault(),
                    "%s %s",
                    "Cancellation Policy: ",
                    travelItem?.cancellationPolicy
                )
                mapFragment.getMapAsync(this)

                val dateFormat = org.threeten.bp.format.DateTimeFormatter.ofPattern("MM/dd/yyyy")

                val bookedDates = entry?.bookedDates?.mapNotNull { dateString ->
                    try {
                        org.threeten.bp.LocalDate.parse(dateString, dateFormat)
                    } catch (e: Exception) {
                        null
                    }
                }

                val bookedDays: List<CalendarDay> = bookedDates?.mapNotNull { localDate ->
                    CalendarDay.from(localDate)
                } ?: emptyList()

                calendarView.addDecorator(CalendarDecorator(Color.RED, bookedDays))
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        myMap = googleMap

        val location = entry?.location
        val latitude = location?.lat
        val longitude = location?.lng

        if (latitude != null && longitude != null) {
            val markerOptions = MarkerOptions()
                .position(LatLng(latitude, longitude))
                .title(toTitleCase(location.name))

            myMap.addMarker(markerOptions)
            val cameraPosition = CameraPosition.Builder()
                .target(LatLng(latitude, longitude))
                .zoom(15.0f)
                .build()

            myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null)
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