package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.configuration.ApiResponse
import com.andrei.finalyearprojectapi.entity.Car
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.redis.*
import com.andrei.finalyearprojectapi.repositories.SimpleCarRepository
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
    private val simpleCarRepository: SimpleCarRepository,
    private val userRepository: UserRepository,
    @Value("\${reservation.timeSeconds}") private val reservationTimeSeconds:Long
) {



    private val commands: RedisCommands<String, String> = redisConnection.sync()

    fun makeReservation(
        car:Car,
        user:User
    ): ApiResponse<Reservation> {
        val keyReservation = RedisKeys.userReservation.format(user.id)
        val keyCar = RedisKeys.car.format(car.id)
        if(commands.keyExists(keyCar)){
            return ApiResponse.Error("Not available")
        }

        val reservationMap = mapOf(
            ReservationKeys.USER_ID.value to user.id.toString(),
            ReservationKeys.CAR_ID.value to car.id.toString()
        )
        commands.apply {
            hmset(
                keyCar,
                mapOf(
                    ReservationKeys.USER_ID.value to user.id.toString(),
                    CarKeys.STATUS.value to CarStatus.RESERVED.value
                )
            )
            hmset(
                keyReservation,
                reservationMap
            )

            expire(keyCar,reservationTimeSeconds)
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
        val reservation = getUserReservation(user) ?: return false
        deleteReservationKeys(
            userID = reservation.user.id,
            carID = reservation.car.id
        )

        return true
    }



     fun getUserReservation(
        user:User
    ):Reservation?{
        //check if user reservation exists
        val keyUserReservation = RedisKeys.userReservation.format(user.id)
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
        Reservation(
            user = userRepository.findByIdOrNull(getValue(ReservationKeys.USER_ID.value).toLong())?: throw Exception(),
            car = simpleCarRepository.findByIdOrNull(getValue(ReservationKeys.CAR_ID.value).toLong())?: throw Exception(),
            remainingTime = remainingTime
        )
    }.getOrNull()






    private fun deleteReservationKeys(
        userID:Long,
        carID:Long
    ){
        commands.apply {
            del(RedisKeys.userReservation.format(userID))
            del(RedisKeys.car.format(carID))
        }
    }


}