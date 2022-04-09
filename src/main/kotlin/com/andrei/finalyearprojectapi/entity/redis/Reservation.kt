package com.andrei.finalyearprojectapi.entity.redis

import com.andrei.finalyearprojectapi.entity.Car
import com.andrei.finalyearprojectapi.entity.User

data class Reservation (
     val user:User,
     val car:Car,
     val remainingTime:Int
 )