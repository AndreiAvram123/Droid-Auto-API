package com.andrei.finalyearprojectapi.request.auth

import com.andrei.finalyearprojectapi.entity.User

data class PaymentRequest(
    val amount:Long,
    val currency:String = "gbp",
    val user:User
)
