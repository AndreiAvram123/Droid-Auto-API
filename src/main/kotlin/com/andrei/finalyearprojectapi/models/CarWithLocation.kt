package com.andrei.finalyearprojectapi.models

import com.andrei.finalyearprojectapi.entity.Car
import com.andrei.finalyearprojectapi.entity.LatLng

data class CarWithLocation(
    val car: Car,
    val location: LatLng
)
