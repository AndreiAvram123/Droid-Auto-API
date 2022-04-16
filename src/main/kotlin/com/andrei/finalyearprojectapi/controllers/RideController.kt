package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.entity.FinishedRide
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.redis.OngoingRide
import com.andrei.finalyearprojectapi.repositories.FinishedRideRepository
import com.andrei.finalyearprojectapi.services.RideService
import com.andrei.finalyearprojectapi.utils.Controllers
import com.andrei.finalyearprojectapi.utils.ApiResponse
import com.andrei.finalyearprojectapi.utils.badRequest
import com.andrei.finalyearprojectapi.utils.okResponse
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class RideController(
    private val rideService: RideService,
    private val finishedRideRepository: FinishedRideRepository
) :BaseRestController(){


    @GetMapping("/rides/ongoing")
    fun getOngoingRide(
        user:User
    ): ApiResponse<OngoingRide?>{
        return okResponse(rideService.getOngoingRide(user))
    }

    @DeleteMapping("/rides/ongoing")
    fun finishRide(
        user:User
    ):ApiResponse<FinishedRide>{
        when(val response = rideService.finishRide(user)){
            is RideService.FinishRideResponse.Success -> {
                return okResponse(
                    response.finishedRide
                )
            }
            RideService.FinishRideResponse.NoRideFound -> {
                return badRequest("")
            }
            RideService.FinishRideResponse.UserInCar -> {
                return badRequest("Get out of the car")
            }
        }
    }

    @GetMapping("/rides/last")
    fun getLastFinishedRide(
       user:User
    ):ApiResponse<FinishedRide?> = okResponse(user.finishedRides.lastOrNull())


    @GetMapping("/rides")
    fun getFinishedRides(
        user:User
    ):ApiResponse<List<FinishedRide>> = okResponse(user.finishedRides)


    @GetMapping("/rides/{id}")
    fun getRideByID(
        @PathVariable("id") id:Long
    ):ApiResponse<FinishedRide>{
        val ride = finishedRideRepository.findTopById(id) ?: return badRequest("No ride found with this id")
        return okResponse(ride)
    }

    override fun registerController() {
        Controllers.add(this::class)
    }
}