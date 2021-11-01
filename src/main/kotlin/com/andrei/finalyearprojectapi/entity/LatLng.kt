package com.andrei.finalyearprojectapi.entity

import javax.persistence.*

@Embeddable
class LatLng(
    val latitude:Double = 0.0,
    val longitude:Double = 0.0
){

}
