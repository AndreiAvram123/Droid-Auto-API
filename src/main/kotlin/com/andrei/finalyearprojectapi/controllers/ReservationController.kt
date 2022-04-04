package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.repositories.CarRepository
import com.andrei.finalyearprojectapi.request.auth.ReservationRequest
import com.andrei.finalyearprojectapi.services.ReservationService
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.noContent
import com.andrei.finalyearprojectapi.utils.okResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class ReservationController (
    private val carRepository: CarRepository,
    private val reservationService: ReservationService
    ){

    @PostMapping("/reservation")
    fun makeReservation(
        @RequestBody
        @Valid
        reservation: ReservationRequest
    ):ResponseWrapper<Nothing>{
        val car = carRepository.findByIdOrNull(reservation.carID) ?: return noContent("No car found with this id")
        reservationService.makeReservation(car)
        return okResponse()
    }

    @PostMapping("/reservation/unlock")
    fun unlockCar():ResponseWrapper<Nothing>{
        return okResponse()
    }

    @DeleteMapping("/reservation")
    fun cancelReservation():ResponseWrapper<Nothing>{
        return okResponse()
    }

    companion object{
        const val carNotAvailableMessage:String  = "Car not available"
    }
}
