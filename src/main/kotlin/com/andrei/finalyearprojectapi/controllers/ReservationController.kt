package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.configuration.ApiResponse
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.redis.Reservation
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
    ):Controller(){


    @PostMapping("/reservation")
    fun makeReservation(
        @RequestBody
        @Valid
        reservation: ReservationRequest,
        user: User
    ):ResponseWrapper<Reservation>{
        val car = carRepository.findByIdOrNull(reservation.carID) ?: return noContent("No car found with this id")
        val reservationResult = reservationService.makeReservation(
            car = car,
            user = user
        )
        return when(reservationResult){
            is ApiResponse.Success -> {
                okResponse(
                    reservationResult.data
                )
            }
            is ApiResponse.Error -> {
                errorResponse(
                    code = HttpStatus.CONFLICT ,
                    error = carNotAvailableMessage)
            }
        }
    }

    @GetMapping("/reservations/current")
    fun getPreReservation(
        user:User
    ):ResponseWrapper<Reservation?> {
        val reservation = reservationService.getUserReservation(user) ?: return okResponse(null)
        return okResponse(
            reservation
        )
    }


    @DeleteMapping("/reservation")
    fun cancelReservation(
        user: User
    ):ResponseWrapper<NoData>{
        val success = reservationService.cancelReservation(user)
        return if(success){
              nothing()
        }else{
            notAcceptable(noReservationFoundMessage)
        }
    }

    companion object{
        const val carNotAvailableMessage:String  = "Car not available at the moment"
        const val noReservationFoundMessage:String  = "No reservation found"
    }

    override fun registerController() {
        Controllers.add(this::class)
    }
}
