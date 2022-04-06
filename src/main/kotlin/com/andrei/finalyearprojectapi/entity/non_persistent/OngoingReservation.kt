package com.andrei.finalyearprojectapi.entity.non_persistent

abstract class Reservation(
    val userID: Long,
    val carID: Long
)

 class PreReservation (
     userID:Long,
     carID:Long,
     val remainingTime:Int
):Reservation(
     userID = userID,
     carID = carID
)

data class RideDetails(
    val startedAt:Long
)

