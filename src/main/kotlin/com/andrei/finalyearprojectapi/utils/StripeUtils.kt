package com.andrei.finalyearprojectapi.utils

import com.stripe.model.Customer
import com.stripe.model.EphemeralKey
import com.stripe.model.PaymentMethodCollection
import com.stripe.net.RequestOptions
import com.stripe.param.CustomerListPaymentMethodsParams
import com.stripe.param.EphemeralKeyCreateParams
import com.stripe.param.PaymentIntentCreateParams
import org.springframework.stereotype.Component


@Component
class StripeUtils {

     fun automaticPaymentMethods(): PaymentIntentCreateParams.AutomaticPaymentMethods=
        PaymentIntentCreateParams.AutomaticPaymentMethods.Builder().setEnabled(true).build()

     fun customerPaymentMethods(customer: Customer): PaymentMethodCollection {
        val params = CustomerListPaymentMethodsParams.builder()
            .setType(CustomerListPaymentMethodsParams.Type.CARD)
            .build()
        return customer.listPaymentMethods(params)
    }

     fun customerKey(customer: Customer): EphemeralKey {
        val keyParams = EphemeralKeyCreateParams.Builder()
            .setCustomer(customer.id)
            .build()
        val requestOptions = RequestOptions.RequestOptionsBuilder().build()
        return  EphemeralKey.create(
            keyParams,
            requestOptions
        )
    }
}