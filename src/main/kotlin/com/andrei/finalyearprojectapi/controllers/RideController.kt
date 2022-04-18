package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.configuration.Response
import com.andrei.finalyearprojectapi.entity.FinishedRide
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.redis.OngoingRide
import com.andrei.finalyearprojectapi.repositories.FinishedRideRepository
import com.andrei.finalyearprojectapi.services.ReservationService
import com.andrei.finalyearprojectapi.services.RideService
import com.andrei.finalyearprojectapi.utils.ApiResponse
import com.andrei.finalyearprojectapi.utils.Controllers
import com.andrei.finalyearprojectapi.utils.badRequest
import com.andrei.finalyearprojectapi.utils.okResponse
import org.springframework.web.bind.annotation.*

@RestController
class RideController(
    private val rideService: RideService,
    private val finishedRideRepository: FinishedRideRepository,
    private val reservationService: ReservationService
) :BaseRestController(){


    @GetMapping("/rides/ongoing")
    fun getOngoingRide(
        user:User
    ): ApiResponse<OngoingRide?>{
        return okResponse(rideService.getOngoingRide(user))
    }

    //todo
    //check if user paid
    @PostMapping("/rides")
   fun startRide(
        user: User
   ):ApiResponse<OngoingRide>{
        val reservation = reservationService.getUserReservation(user) ?: return badRequest("Cannot start ride , no reservation found")
        val response =  rideService.startRide(
            reservation
        )
        return when(response){
            is Response.Error -> badRequest(response.error)
            is Response.Success -> okResponse(response.data)
        }
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