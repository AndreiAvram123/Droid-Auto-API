package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.request.auth.PaymentRequest
import com.andrei.finalyearprojectapi.response.PaymentResponse
import com.stripe.Stripe
import com.stripe.model.Customer
import com.stripe.model.PaymentIntent
import com.stripe.model.PaymentMethodCollection
import com.stripe.param.CustomerCreateParams
import com.stripe.param.CustomerListPaymentMethodsParams
import com.stripe.param.PaymentIntentCreateParams
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class PaymentService(
    @Value("\${paymentService.stripeSecretKey}")
    private val stripeSecretKey:String,
    @Value("\${paymentService.publishableKey}")
    private val publishableKey:String,
    private val userRepository: UserRepository
) {
    init {
        Stripe.apiKey = stripeSecretKey
    }

    private fun automaticPaymentMethods():PaymentIntentCreateParams.AutomaticPaymentMethods=
        PaymentIntentCreateParams.AutomaticPaymentMethods.Builder().setEnabled(true).build()

    private fun customerPaymentMethods(customer: Customer):PaymentMethodCollection{
        val params = CustomerListPaymentMethodsParams.builder()
            .setType(CustomerListPaymentMethodsParams.Type.CARD)
            .build()
        return customer.listPaymentMethods(params)
    }

    fun createPaymentResponse(
        paymentRequest: PaymentRequest
    ):PaymentResponse{

        //customer object for user
        val customer = getStripeCustomer(paymentRequest.user) ?: createStripeCustomer(paymentRequest.user)
        val paymentIntentParameters = PaymentIntentCreateParams.Builder().apply {
              setAmount(paymentRequest.amount)
              setAutomaticPaymentMethods(automaticPaymentMethods())
              setCustomer(customer.id)
              setCurrency("gbp")
          }.build()
        val paymentIntent = PaymentIntent.create(paymentIntentParameters)
        return paymentIntent.toPaymentResponse()

    }

    fun chargeFuturePayment(
        paymentRequest: PaymentRequest
    ):Result<PaymentIntent> {
        val customer = getStripeCustomer(paymentRequest.user) ?: createStripeCustomer(paymentRequest.user)
        val paymentMethod = customerPaymentMethods(customer).data.first()
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


    fun PaymentIntent.toPaymentResponse(): PaymentResponse{
        return PaymentResponse(
            clientSecret = clientSecret,
            publishableKey = publishableKey
        )
    }

}