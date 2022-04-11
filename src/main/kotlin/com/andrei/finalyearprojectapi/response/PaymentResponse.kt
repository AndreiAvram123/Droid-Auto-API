package com.andrei.finalyearprojectapi.response

data class PaymentResponse(
    val clientSecret:String,
    val publishableKey:String,
    val customerID:String,
    val customerKey:String

)
