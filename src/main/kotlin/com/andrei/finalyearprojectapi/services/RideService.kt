package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.models.OngoingRide
import io.lettuce.core.api.StatefulRedisConnection
import org.springframework.stereotype.Service

interface RideService{
    fun getOngoingRide(user:User):OngoingRide
}

@Service
class RideServiceImpl(
    redisConnection: StatefulRedisConnection<String, String>,
) :RideService{
    private val commands = redisConnection.sync()

    override fun getOngoingRide(user:User): OngoingRide {
        return OngoingRide(0,0)
    }

}