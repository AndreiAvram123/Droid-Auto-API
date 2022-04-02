package com.andrei.finalyearprojectapi.models

import com.google.gson.annotations.SerializedName

data class DirectionStep(
     @SerializedName(value = "endLocation", alternate = ["end_location"])
     val endLocation:LatLngDTO,
     @SerializedName(value = "startLocation", alternate = ["start_location"])
     val startLocation:LatLngDTO
)
