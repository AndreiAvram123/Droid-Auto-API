package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.request.auth.PaymentRequest
import com.andrei.finalyearprojectapi.response.PaymentResponse
import com.andrei.finalyearprojectapi.utils.StripeUtils
import com.stripe.Stripe
import com.stripe.model.Customer
import com.stripe.model.PaymentIntent
import com.stripe.param.CustomerCreateParams
import com.stripe.param.PaymentIntentCreateParams
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class PaymentService(
    @Value("\${paymentService.stripeSecretKey}")
    private val stripeSecretKey:String,
    @Value("\${paymentService.publishableKey}")
    private val publishableKey:String,
    private val userRepository: UserRepository,
    private val stripeUtils:StripeUtils
) {
    init {
        Stripe.apiKey = stripeSecretKey
    }


    fun createPaymentResponse(
        paymentRequest: PaymentRequest
    ):PaymentResponse{

        //customer object for user
        val customer = getStripeCustomer(paymentRequest.user) ?: createStripeCustomer(paymentRequest.user)
        val paymentIntentParameters = PaymentIntentCreateParams.Builder().apply {
              setAmount(paymentRequest.amount)
              setAutomaticPaymentMethods(stripeUtils.automaticPaymentMethods())
              setCustomer(customer.id)
              setCurrency("gbp")
          }.build()
        val paymentIntent = PaymentIntent.create(paymentIntentParameters)
        return  PaymentResponse(
                clientSecret = paymentIntent.clientSecret,
                publishableKey = publishableKey,
                customerID = customer.id,
                customerKey = stripeUtils.customerKey(customer).id
        )

    }

    fun chargeFuturePayment(
        paymentRequest: PaymentRequest
    ):Result<PaymentIntent> {
        val customer = getStripeCustomer(paymentRequest.user) ?: createStripeCustomer(paymentRequest.user)
        val paymentMethod = stripeUtils.customerPaymentMethods(customer).data.first()
        val paymentIntentParameters = PaymentIntentCreateParams.Builder().apply {
            setPaymentMethod(paymentMethod.id)
            setAmount(paymentRequest.amount)
            setCustomer(customer.id)
            setOffSession(true)
            setConfirm(true)
            setCurrency("gbp")
        }.build()
        return runCatching {
            PaymentIntent.create(paymentIntentParameters)
        }.onFailure {
           println(it.message)
        }
    }

    fun getStripeCustomer(
        user: User
    ):Customer?{
        if(user.stripeCustomerID == null){
            return null
        }
        return Customer.retrieve(user.stripeCustomerID)
    }

    fun createStripeCustomer(user:User):Customer{
       val customerCreateParams = CustomerCreateParams.builder()
           .setEmail(user.email)
           .setName("${user.firstName} ${user.lastName}")
           .build()
       val customer = Customer.create(customerCreateParams)
        user.stripeCustomerID = customer.id
        userRepository.save(user)
        return customer

    }



}