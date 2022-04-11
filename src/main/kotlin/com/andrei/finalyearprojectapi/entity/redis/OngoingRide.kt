package com.andrei.finalyearprojectapi.entity.redis

import com.andrei.finalyearprojectapi.entity.Car
import com.andrei.finalyearprojectapi.entity.FinishedRide
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.utils.unixTime

data class OngoingRide(
    val user: User,
    val car: Car,
    //unix time seconds
    val startTime:Long
    )

fun OngoingRide.elapsedSeconds():Long = unixTime() - this.startTime

fun OngoingRide.totalCharge():Long  = elapsedSeconds() * car.pricePerMinute

fun OngoingRide.toFinishedRide():FinishedRide = FinishedRide(
    id = 0,
    totalCharge = totalCharge(),
    startTime = startTime,
    endTime = unixTime(),
    user = user,
    car = car
)

