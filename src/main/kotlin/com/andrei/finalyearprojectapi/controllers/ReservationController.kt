package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.repositories.CarRepository
import com.andrei.finalyearprojectapi.request.auth.ReservationRequest
import com.andrei.finalyearprojectapi.services.ReservationService
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.errorResponse
import com.andrei.finalyearprojectapi.utils.noContent
import com.andrei.finalyearprojectapi.utils.okResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
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
        reservation: ReservationRequest,
        user: User
    ):ResponseWrapper<Nothing>{
        val car = carRepository.findByIdOrNull(reservation.carID) ?: return noContent("No car found with this id")
        val reservationResult = reservationService.makeReservation(
            car = car,
            user = user
        )
        when(reservationResult){
            is ReservationService.ReservationResult.Reserved -> {
                return okResponse()
            }
            is ReservationService.ReservationResult.NotAvailable -> {
                return errorResponse(
                    code = HttpStatus.CONFLICT ,
                    error = carNotAvailableMessage)
            }
        }
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
        const val carNotAvailableMessage:String  = "Car not available at the moment"
    }
}
