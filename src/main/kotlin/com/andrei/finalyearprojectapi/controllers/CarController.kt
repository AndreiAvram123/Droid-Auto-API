package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.entity.Car
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.repositories.CarRepository
import com.andrei.finalyearprojectapi.services.ReservationService
import com.andrei.finalyearprojectapi.services.RideService
import com.andrei.finalyearprojectapi.utils.LocationUtils
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.badRequest
import com.andrei.finalyearprojectapi.utils.okResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CarController(
    private val carRepository: CarRepository,
    @Value("\${nearbyCarsDistanceMeters}")
    private val nearbyCarsDistance:Long,
    private val rideService: RideService,
    private val reservationService: ReservationService
) {
    @GetMapping("/nearby")
    fun getNearbyCars(
        @RequestParam latitude:Double,
        @RequestParam longitude:Double
    ):ResponseWrapper<List<Car>>{

        //todo
        //add redis logic here
        return okResponse(
            carRepository.findAll().filter { car->
                LocationUtils.distance(
                    lat1 = car.location.latitude,
                    lon1 = car.location.longitude,
                    lat2 = latitude,
                    lon2 = longitude
                ) < nearbyCarsDistance
            }
        )
    }

    @PostMapping("/car/unlock")
    fun unlockCar(
        user:User
    ):ResponseWrapper<Nothing>{
        val reservation = reservationService.getUserReservation(user) ?: return badRequest("Cannot unlock car")
        rideService.startRide(
            reservation
        )
        return okResponse()
    }

}
