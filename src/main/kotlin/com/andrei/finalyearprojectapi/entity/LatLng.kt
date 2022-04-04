package com.andrei.finalyearprojectapi.entity

import javax.persistence.Embeddable

@Embeddable
class LatLng(
    val latitude:Double,
    val longitude:Double
)
