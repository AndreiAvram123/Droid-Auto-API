package com.andrei.finalyearprojectapi.entity.redis

import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.models.CarWithLocation

data class Reservation (
     val user:User,
     val carWithLocation: CarWithLocation,
     val remainingTime:Int
 )