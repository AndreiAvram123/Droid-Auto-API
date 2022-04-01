package com.andrei.finalyearprojectapi.request.auth

data class DirectionsRequest(
    val startLatitude:Double,
    val endLatitude:Double,
    val startLongitude:Double,
    val endLongitude:Double
)
