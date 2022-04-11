package com.andrei.finalyearprojectapi.entity.redis

import com.andrei.finalyearprojectapi.entity.Car
import com.andrei.finalyearprojectapi.entity.FinishedRide
import com.andrei.finalyearprojectapi.entity.User

data class OngoingRide(
    val user: User,
    val car: Car,
    val startedTime:Long
    )

fun OngoingRide.elapsedSeconds():Long = System.currentTimeMillis()/1000L - this.startedTime

fun OngoingRide.totalCharge():Double  = elapsedSeconds() * car.pricePerMinute

fun OngoingRide.toFinishedRide():FinishedRide = FinishedRide(
    id = 0,
    totalCharge = totalCharge(),
    startedTime = startedTime,
    user = user,
    car = car
)

