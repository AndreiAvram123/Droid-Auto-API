package com.andrei.car_rental_android.engine.response

import com.andrei.finalyearprojectapi.entity.non_persistent.TemporaryReservation

data class ReservationResponse(
    val isTemporary:Boolean,
    val temporaryReservation: TemporaryReservation?
)
