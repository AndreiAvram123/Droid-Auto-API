package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.configuration.ApiResponse
import com.andrei.finalyearprojectapi.entity.Car
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.redis.*
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.utils.hasExpireTime
import com.andrei.finalyearprojectapi.utils.keyExists
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisCommands
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ReservationService(
    redisConnection: StatefulRedisConnection<String, String>,
    private val carWithLocationRepository: CarWithLocationRepository,
    private val userRepository: UserRepository,
    @Value("\${reservation.timeSeconds}") private val reservationTimeSeconds:Long
) {



    private val commands: RedisCommands<String, String> = redisConnection.sync()


    fun makeReservation(
        car:Car,
        user:User
    ): ApiResponse<Reservation> {
        val keyReservation = RedisKeys.userCarReservation.format(user.id)
        val keyCar = RedisKeys.car.format(car.id)

        val reservationMap = mapOf(
            ReservationKeys.USER_ID.value to user.id.toString(),
            ReservationKeys.CAR_ID.value to car.id.toString()
        )
        commands.apply {
            hmset(
                keyCar,
                mapOf(
                    CarKeys.STATUS.value to CarStatus.RESERVED.value
                )
            )
            hmset(
                keyReservation,
                reservationMap
            )

            expire(keyReservation,reservationTimeSeconds)
        }
        val reservation = reservationMap.toReservation(
            commands.ttl(keyReservation).toInt()
        )?: return ApiResponse.Error("System error")


        return ApiResponse.Success(reservation)
    }

    fun cancelReservation(
        user:User
    ):Boolean{
         getUserReservation(user) ?: return false
        commands.del(RedisKeys.userCarReservation.format(user.id))

        return true
    }



     fun getUserReservation(
        user:User
    ):Reservation?{
        //check if user reservation exists
        val keyUserReservation = RedisKeys.userCarReservation.format(user.id)
        if(commands.keyExists(keyUserReservation)){
            val reservationMap:Map<String,String> = commands.hgetall(keyUserReservation)
            if(commands.hasExpireTime(keyUserReservation)){
                 //pre reservation
                  return reservationMap.toReservation(
                      commands.ttl(keyUserReservation).toInt()
                  )
            }
        }
         return null
    }
    private fun Map<String,String>.toReservation(
        remainingTime:Int
    ):Reservation? = runCatching{
        //todo
        //a reservation should not have the location
        //that should be obtained dynamically
        val carWithLocation = carWithLocationRepository.findByIdOrNull(getValue(ReservationKeys.CAR_ID.value).toLong()) ?: throw Exception()

        Reservation(
            user = userRepository.findByIdOrNull(getValue(ReservationKeys.USER_ID.value).toLong())?: throw Exception(),
            car = carWithLocation.car,
            carLocation = carWithLocation.location,
            remainingTime = remainingTime
        )
    }.getOrNull()






}