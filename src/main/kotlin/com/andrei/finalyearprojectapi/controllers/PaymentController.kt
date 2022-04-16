package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.request.auth.PaymentRequest
import com.andrei.finalyearprojectapi.response.PaymentResponse
import com.andrei.finalyearprojectapi.services.PaymentService
import com.andrei.finalyearprojectapi.utils.Controllers
import com.andrei.finalyearprojectapi.utils.ApiResponse
import com.andrei.finalyearprojectapi.utils.okResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PaymentController(
    private val paymentService: PaymentService
) :BaseRestController(){

    private val unlockFee = 200L
    @PostMapping("/payment/unlock_fee")
    fun createPayment(
        user:User
    ):ApiResponse<PaymentResponse>{
        val paymentRequest = PaymentRequest(
            amount = unlockFee,
            user = user
        )
       val paymentResponse =  paymentService.createPaymentResponse(paymentRequest)

      return okResponse(paymentResponse)
    }

    final override fun registerController() {
        Controllers.add(this::class)
    }
}