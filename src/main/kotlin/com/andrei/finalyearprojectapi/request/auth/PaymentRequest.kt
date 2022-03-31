package com.andrei.finalyearprojectapi.request.auth

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class PaymentRequest(
    /**
     * Amount intended to be collected by this PaymentIntent. A positive integer representing how much to charge in the smallest currency unit  (e.g., 100 cents to charge $1.00 or 100 to charge ¥100, a zero-decimal currency). The minimum amount is $0.50 US or equivalent in charge currency . The amount value supports up to eight digits (e.g., a value of 99999999 for a USD charge of $999,999.99).
     */
    @Min(value = 50)
    val amount:Long,
    @NotBlank
    val currency:String = "gbp"
)
