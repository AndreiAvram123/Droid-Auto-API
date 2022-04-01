package com.andrei.finalyearprojectapi.models

import com.google.gson.annotations.SerializedName

data class DirectionStep(
     @SerializedName("end_location")
     val endLocation:LatLngDTO,
     @SerializedName("start_location")
     val startLocation:LatLngDTO
)
