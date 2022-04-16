package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.configuration.Response
import com.andrei.finalyearprojectapi.entity.LatLng
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.redis.OngoingRide
import com.andrei.finalyearprojectapi.models.CarWithLocation
import com.andrei.finalyearprojectapi.repositories.SimpleCarRepository
import com.andrei.finalyearprojectapi.services.CarLocationService
import com.andrei.finalyearprojectapi.services.CarWithLocationRepository
import com.andrei.finalyearprojectapi.services.ReservationService
import com.andrei.finalyearprojectapi.services.RideService
import com.andrei.finalyearprojectapi.utils.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.*

@RestController
class CarController(
    private val carWithLocationRepository: CarWithLocationRepository,
    @Value("\${nearbyCarsDistanceMeters}")
    private val nearbyCarsDistance:Long,
    private val rideService: RideService,
    private val reservationService: ReservationService,
    private val simpleCarRepository: SimpleCarRepository,
    private val carLocationService:CarLocationService
) :BaseRestController(){


    @GetMapping("/nearby")
    fun getNearbyCars(
        @RequestParam latitude:Double,
        @RequestParam longitude:Double
    ):ApiResponse<List<CarWithLocation>>{

        //todo
        //make sure these are not reserved
        return okResponse(
            carWithLocationRepository.findAll().filter { car->
                LocationUtils.distance(
                    lat1 = car.location.latitude,
                    lon1 = car.location.longitude,
                    lat2 = latitude,
                    lon2 = longitude
                ) < nearbyCarsDistance
            }
        )
    }


    @GetMapping("/cars/{id}/location")
    fun getCarLocation(
        @PathVariable("id") carID:Long
    ):ApiResponse<LatLng?>{
         val car = simpleCarRepository.findByIdOrNull(carID) ?: return badRequest("Car does not exist");
         val location = carLocationService.getCarLocation(car)
         return okResponse(location)
    }


    //todo
    //unlocking should happen only if user paid

    @PostMapping("/car/unlock")
    fun unlockCar(
        user:User
    ):ApiResponse<OngoingRide>{
        val reservation = reservationService.getUserReservation(user) ?: return badRequest("Cannot unlock car")
        val response =  rideService.startRide(
            reservation
        )
        return when(response){
            is Response.Error -> badRequest("Cannot unlock car")
            is Response.Success -> okResponse(response.data)
        }
    }

    final override fun registerController() {
        Controllers.add(this::class)
    }

}
