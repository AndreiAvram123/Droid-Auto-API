package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.request.auth.PaymentRequest
import com.andrei.finalyearprojectapi.response.PaymentResponse
import com.stripe.Stripe
import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class PaymentService(
    @Value("\${paymentService.stripeSecretKey}")
    private val stripeSecretKey:String,
    @Value("\${paymentService.publishableKey}")
    private val publishableKey:String,
) {
    init {
        Stripe.apiKey = stripeSecretKey
    }

    fun createPaymentResponse(
        paymentRequest: PaymentRequest
    ):PaymentResponse{
          val paymentIntentParameters = PaymentIntentCreateParams.Builder().apply {
              setAmount(paymentRequest.amount *100)
              setCurrency("gbp")
          }.build()
        val paymentIntent = PaymentIntent.create(paymentIntentParameters)
        return paymentIntent.toPaymentResponse()

    }

    fun PaymentIntent.toPaymentResponse(): PaymentResponse{
        return PaymentResponse(
            clientSecret = clientSecret,
            publishableKey = publishableKey
        )
    }
}