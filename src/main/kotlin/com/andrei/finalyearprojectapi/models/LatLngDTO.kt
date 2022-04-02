package com.andrei.finalyearprojectapi.models

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class LatLngDTO(
    @SerializedName(value ="lat", alternate = ["latitude"])
    @JsonProperty("lat")
    val latitude:Double,
    @SerializedName(value = "lng", alternate = ["longitude"])
    @JsonProperty("lng")
    val longitude:Double
)