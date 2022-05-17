package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.utils.ApiResponse
import com.andrei.finalyearprojectapi.utils.NoData
import com.andrei.finalyearprojectapi.utils.errorResponse
import com.andrei.finalyearprojectapi.utils.nothing
import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Email
import com.sendgrid.helpers.mail.objects.Personalization
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service


@Service
class EmailService(

    @Value("\${emailService.apiKey}") private val apiKey:String,
    @Value("\${emailService.fromIdentity}") private val fromIdentity:String
) {

    private val sendGrid = SendGrid(apiKey)
    private val confirmationEmailTemplateID = "d-ee08b950a5fa42ce8da56874f06e76aa"

    fun sendVerificationEmail(
        to:Email
    ):ApiResponse<NoData>{

        val mail = Mail().apply {
            subject = "Verify your email"
            templateId = confirmationEmailTemplateID
            from = Email(fromIdentity)
        }

        val confirmationEmailData = ConfirmationEmailData(
            confirmURL = "https://car-rental-api-dev.herokuapp.com/email/verification?uuid=${to.email}"
        )
        val personalization = Personalization().apply {
            addTo(to)

            addDynamicTemplateData(ConfirmationEmailData.confirmURLKey,confirmationEmailData.confirmURL)
        }

        mail.addPersonalization(personalization)

        val request = Request().apply {
            method = Method.POST
            endpoint = "mail/send"
            body = mail.build()

        }
        return try{
            val response = sendGrid.api(request)
            nothing()
        }catch (e:Exception){
            errorResponse(
                code = HttpStatus.INTERNAL_SERVER_ERROR,
                error =  e.localizedMessage ?: "Unknown error")
        }
        }


     class ConfirmationEmailData(
          val confirmURL:String
    ){

        companion object{
             const val confirmURLKey:String = "confirmURL"
        }
    }
}
