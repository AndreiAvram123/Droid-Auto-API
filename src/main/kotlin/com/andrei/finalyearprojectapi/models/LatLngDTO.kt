package com.andrei.finalyearprojectapi.models

import com.google.gson.annotations.SerializedName

data class LatLngDTO(
    @SerializedName(value ="lat", alternate = ["latitude"])
    val latitude:Double,
    @SerializedName(value = "lng", alternate = ["longitude"])
    val longitude:Double
)