package com.andrei.finalyearprojectapi.entity

import javax.persistence.*

@Embeddable
class LatLng(
    val latitude:Long = 0,
    val longitude:Long = 0
){

}
