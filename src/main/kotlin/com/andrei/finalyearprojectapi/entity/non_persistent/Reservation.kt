package com.andrei.finalyearprojectapi.entity.non_persistent

import com.andrei.finalyearprojectapi.entity.Car

 data class Reservation (
     val userID:Long,
     val car:Car,
     val remainingTime:Int
 )