package com.grayseal.travelhub.data.model

data class TravelItem(
    val __v: Int,
    val _id: String,
    val amenities: List<String>,
    val bookedDates: List<String>,
    val bookings: Int,
    val cancellationPolicy: String,
    val created: String,
    val description: String,
    val details: Details,
    val emergencyBooking: EmergencyBooking,
    val location: Location,
    val name: String,
    val noOfRates: Int,
    val photos: List<String>,
    val point: Point,
    val price: Price,
    val rating: Int,
    val recommendations: Int,
    val reviews: List<Any>,
    val rules: Rules,
    val space: String,
    val status: String,
    val tags: List<String>,
    val timeStamp: Long,
    val type: String,
    val uniqueType: String,
    val video: String
)