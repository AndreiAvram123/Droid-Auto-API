package com.andrei.finalyearprojectapi.services

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Email
import com.sendgrid.helpers.mail.objects.Personalization
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class EmailService(

    @Value("\${emailService.apiKey}") private val apiKey:String,
    @Value("\${emailService.fromIdentity}") private val fromIdentity:String
) {

    private val sendGrid = SendGrid(apiKey)
    private val confirmationEmailTemplateID = "d-ee08b950a5fa42ce8da56874f06e76aa"

    fun sendConfirmationEmail(
        to:Email
    ){

        val mail = Mail().apply {
            subject = "Let's test this"
            templateId = confirmationEmailTemplateID
            from = Email(fromIdentity)
        }
        val personalization = Personalization().apply {
            addTo(to)
        }
        mail.addPersonalization(personalization)

        val request = Request().apply {
            method = Method.POST
            endpoint = "mail/send"
            body = mail.build()

        }
        try{
          val response = sendGrid.api(request)
            print(response.statusCode)
        }catch (e:Exception){
            print(e)
        }
        }

}
