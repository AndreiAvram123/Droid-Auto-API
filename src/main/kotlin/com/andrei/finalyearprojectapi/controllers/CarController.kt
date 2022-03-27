package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.entity.Car
import com.andrei.finalyearprojectapi.repositories.CarRepository
import com.andrei.finalyearprojectapi.request.auth.ReservationRequest
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.badRequest
import com.andrei.finalyearprojectapi.utils.okResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class CarController(
    private val carRepository: CarRepository
) {

    @GetMapping("/nearby")
    fun getNearbyCars():ResponseWrapper<List<Car>>{
        return okResponse(
            carRepository.findAll()
        )
    }

    @PostMapping("/reservation")
    fun makeReservation(
        @RequestBody
        @Valid
        reservation: ReservationRequest
    ):ResponseWrapper<Nothing>{
         val car = carRepository.findByIdOrNull(reservation.carID) ?: return badRequest("No car found with this id")
        return okResponse()
    }
}