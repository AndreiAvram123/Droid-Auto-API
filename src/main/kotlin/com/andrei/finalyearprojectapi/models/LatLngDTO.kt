package com.andrei.finalyearprojectapi.models

import com.google.gson.annotations.SerializedName

data class LatLngDTO(
    @SerializedName("lat")
    val latitude:Double,
    @SerializedName("lng")
    val longitude:Double
)