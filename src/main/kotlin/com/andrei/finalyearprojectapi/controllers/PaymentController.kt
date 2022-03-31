package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.request.auth.PaymentRequest
import com.andrei.finalyearprojectapi.response.PaymentResponse
import com.andrei.finalyearprojectapi.services.PaymentService
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.okResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PaymentController(
    private val paymentService: PaymentService
) {

    private val unlockFee = 200L
    @PostMapping("/payment/unlock_fee")
    fun createPayment():ResponseWrapper<PaymentResponse>{
        val paymentRequest = PaymentRequest(
            amount = unlockFee
        )
       val paymentResponse =  paymentService.createPaymentResponse(paymentRequest)

      return okResponse(paymentResponse)
    }
}