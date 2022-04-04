package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.entity.Car
import com.andrei.finalyearprojectapi.entity.User
import com.google.gson.Gson
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisCommands
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ReservationService(
     redisConnection: StatefulRedisConnection<String, String>,
     @Value("\${reservation.timeSeconds}") private val reservationTimeSeconds:Long,
     private val gson: Gson
) {

    sealed class ReservationResult{
        object NotAvailable:ReservationResult()
        object Reserved:ReservationResult()
    }

    private val commands: RedisCommands<String, String> = redisConnection.sync()

    fun makeReservation(
        car:Car,
        user:User
    ):ReservationResult {
        val key = "carID:${car.id}"
        if(reservationExists(key)){
            return ReservationResult.NotAvailable
        }
        commands.hset(
             key,
               mapOf(
                   "userID" to user.id.toString()
               )
        )
        commands.expire(key,reservationTimeSeconds)
        return ReservationResult.Reserved
    }

   private fun reservationExists(key:String):Boolean {
       return commands.exists(key) > 0
   }

}