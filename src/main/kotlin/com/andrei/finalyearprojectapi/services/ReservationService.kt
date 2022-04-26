package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.configuration.Response
import com.andrei.finalyearprojectapi.entity.Car
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.redis.*
import com.andrei.finalyearprojectapi.repositories.SimpleCarRepository
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
    @Value("\${reservation.timeSeconds}") private val reservationTimeSeconds:Long
) {



    private val commands: RedisCommands<String, String> = redisConnection.sync()


    fun makeReservation(
        car:Car,
        user:User
    ): Response<Reservation> {
        //this should really be called by the client before
        //but this is a defensive check
        val currentReservation = getUserReservation(user)
        if(currentReservation != null){
            return Response.Error("Reservation exists")
        }

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
        val reservation = Reservation(
            user = user,
            car = car,
            commands.ttl(keyReservation).toInt()
        )

        return Response.Success(reservation)
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
                val carID = reservationMap[ReservationKeys.CAR_ID.value]?.toLong() ?: return null
                val car = simpleCarRepository.findByIdOrNull(carID) ?: return null
                return   Reservation(
                    user = user,
                    car = car,
                    commands.ttl(keyUserReservation).toInt()
                )
            }
        }
        return null
    }





}