package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.configuration.Response
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.redis.Reservation
import com.andrei.finalyearprojectapi.repositories.SimpleCarRepository
import com.andrei.finalyearprojectapi.request.auth.ReservationRequest
import com.andrei.finalyearprojectapi.services.ReservationService
import com.andrei.finalyearprojectapi.utils.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
class ReservationController (
    private val simpleCarRepository: SimpleCarRepository,
    private val reservationService: ReservationService
    ):BaseRestController(){


    @PostMapping("/reservations")
    fun makeReservation(
        @RequestBody
        @Valid
        reservation: ReservationRequest,
        user: User
    ):ApiResponse<Reservation>{
        val car = simpleCarRepository.findByIdOrNull(reservation.carID) ?: return noContent("No car found with this id")
        reservationService.getUserReservation(user)?.let {
            return errorResponse(
                code = HttpStatus.CONFLICT ,
                error = carNotAvailableMessage
            )
        }
        val reservationResult = reservationService.makeReservation(
            car = car,
            user = user
        )

        return when(reservationResult){
            is Response.Success -> {
                okResponse(
                    reservationResult.data
                )
            }
            is Response.Error -> {
                errorResponse(
                    code = HttpStatus.CONFLICT ,
                    error = carNotAvailableMessage)
            }
        }
    }

    @GetMapping("/reservations/current")
    fun getCurrentReservation(
        user:User
    ):ApiResponse<Reservation?> {
        val reservation = reservationService.getUserReservation(user) ?: return okResponse(null)
        return okResponse(
            reservation
        )
    }


    @DeleteMapping("/reservations/current")
    fun cancelReservation(
        user: User
    ):ApiResponse<NoData>{
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
