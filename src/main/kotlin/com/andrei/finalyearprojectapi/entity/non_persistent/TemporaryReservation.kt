package com.andrei.finalyearprojectapi.entity.non_persistent

import com.andrei.finalyearprojectapi.entity.Car

abstract class Reservation(
    val userID: Long,
    val car:Car
)

 class TemporaryReservation (
     userID:Long,
     car:Car,
     val remainingTime:Int
):Reservation(
     userID = userID,
     car = car
)
class CompleteReservation(  userID:Long, car:Car,):Reservation(
    userID = userID,
    car = car,
)


