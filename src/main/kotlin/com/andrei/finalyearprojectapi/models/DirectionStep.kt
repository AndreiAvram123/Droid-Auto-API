package com.andrei.finalyearprojectapi.models

import com.andrei.finalyearprojectapi.entity.LatLng
import com.google.gson.annotations.SerializedName

data class DirectionStep(
     @SerializedName(value = "endLocation", alternate = ["end_location"])
     val endLocation:LatLng,
     @SerializedName(value = "startLocation", alternate = ["start_location"])
     val startLocation:LatLng
)
