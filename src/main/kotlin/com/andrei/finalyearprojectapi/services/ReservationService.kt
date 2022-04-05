package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.entity.Car
import com.andrei.finalyearprojectapi.entity.User
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisCommands
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ReservationService(
     redisConnection: StatefulRedisConnection<String, String>,
     @Value("\${reservation.timeSeconds}") private val reservationTimeSeconds:Long
) {

    enum class ReservationFieldKeys(val value:String) {
        USER_ID("userID"), CAR_ID("carID")
    }

    private val userKeyFormat = "reservation_user_%d"
    private val carKeyFormat ="reservation_car_%d"

    sealed class ReservationResult{
        object NotAvailable:ReservationResult()
        object Reserved:ReservationResult()
    }

    private val commands: RedisCommands<String, String> = redisConnection.sync()

    fun makePreReservation(
        car:Car,
        user:User
    ):ReservationResult {
        val keyUser = userKeyFormat.format(user.id)
        val keyCar = carKeyFormat.format(car.id)
        if(reservationExists(car.id)){
            return ReservationResult.NotAvailable
        }
        commands.apply {
            hmset(
                keyCar,
                mapOf(
                    ReservationFieldKeys.USER_ID.value to user.id.toString(),
                    ReservationFieldKeys.CAR_ID.value to car.id.toString()
                )
            )
            hmset(
                keyUser,
                mapOf(
                    ReservationFieldKeys.USER_ID.value to user.id.toString(),
                    ReservationFieldKeys.CAR_ID.value to car.id.toString()
                )
            )
            expire(keyCar,reservationTimeSeconds)
            expire(keyUser,reservationTimeSeconds)

        }

        return ReservationResult.Reserved
    }

   private fun reservationExists(carID:Long):Boolean {
       return commands.exists("carID:${carID}") > 0
   }

    fun cancelReservation(
        user:User
    ):Boolean{
        //check if user reservation exists
        val keyUserReservation = userKeyFormat.format(user.id)

        if(commands.exists(keyUserReservation) < 1) {
           return false
        }
        val carReservedID = commands.hget(keyUserReservation,ReservationFieldKeys.CAR_ID.value) ?: return false
        val keyCarReservation = carKeyFormat.format(carReservedID.toLong())
        if(commands.exists(keyCarReservation) > 0){
            deleteReservationKeys(
                userID = user.id,
                carID = carReservedID.toLong()
            )
            return true
        }
        return false
    }

    private fun deleteReservationKeys(
        userID:Long,
        carID:Long
    ){
        commands.apply {
            del(userKeyFormat.format(userID))
            del(carKeyFormat.format(carID))
        }
    }

}