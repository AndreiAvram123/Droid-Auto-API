package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.entity.FinishedRide
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.redis.OngoingRide
import com.andrei.finalyearprojectapi.services.RideService
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.badRequest
import com.andrei.finalyearprojectapi.utils.okResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class RideController(
    private val rideService: RideService
) {


    @GetMapping("/rides/ongoing")
    fun getOngoingRide(
        user:User
    ): ResponseWrapper<OngoingRide?>{
        return okResponse(rideService.getOngoingRide(user))
    }

    @DeleteMapping("/rides/ongoing")
    fun finishRide(
        user:User
    ):ResponseWrapper<FinishedRide>{
        when(val response = rideService.finishRide(user)){
            is RideService.FinishRideResponse.Success -> {
                return okResponse(
                    response.finishedRide
                )
            }
            RideService.FinishRideResponse.NoRideFound -> {

            }
            RideService.FinishRideResponse.UserInCar -> {


            }
        }
        return badRequest("")
    }

}