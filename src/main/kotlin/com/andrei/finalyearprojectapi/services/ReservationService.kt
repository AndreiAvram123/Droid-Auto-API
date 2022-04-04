package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.entity.Car
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.sync.RedisCommands
import org.springframework.stereotype.Service

@Service
class ReservationService(
     redisConnection: StatefulRedisConnection<String, String>
) {



    private val commands: RedisCommands<String, String> = redisConnection.sync()

    fun makeReservation(car:Car){
       commands.set(car.id.toString(), "fds")
    }

}