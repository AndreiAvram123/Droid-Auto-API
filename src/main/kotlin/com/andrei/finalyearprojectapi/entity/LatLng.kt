package com.andrei.finalyearprojectapi.entity

import com.google.gson.annotations.SerializedName

class LatLng(
    @SerializedName(value ="lat", alternate = ["latitude"])
    val latitude:Double,
    @SerializedName(value = "lng", alternate = ["longitude"])
    val longitude:Double
)
