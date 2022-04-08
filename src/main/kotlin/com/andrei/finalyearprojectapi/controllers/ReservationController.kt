package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.non_persistent.Reservation
import com.andrei.finalyearprojectapi.repositories.CarRepository
import com.andrei.finalyearprojectapi.request.auth.ReservationRequest
import com.andrei.finalyearprojectapi.services.ReservationService
import com.andrei.finalyearprojectapi.utils.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
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
        val reservationResult = reservationService.makePreReservation(
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

    @GetMapping("/reservations/current")
    fun getPreReservation(
        user:User
    ):ResponseWrapper<Reservation> {
        val reservation = reservationService.getUserReservation(user) ?: return okResponse(null)
        return okResponse(
            reservation
        )
    }

    @PostMapping("/reservation/car/unlock")
    fun unlockCar():ResponseWrapper<Nothing>{
        return okResponse()
    }

    @DeleteMapping("/reservation")
    fun cancelReservation(
        user: User
    ):ResponseWrapper<Nothing>{
        val success = reservationService.cancelReservation(user)
        return if(success){
            okResponse()
        }else{
            notAcceptable(noReservationFoundMessage)
        }
    }

    companion object{
        const val carNotAvailableMessage:String  = "Car not available at the moment"
        const val noReservationFoundMessage:String  = "No reservation found"
    }
}
