package com.andrei.finalyearprojectapi.services

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGrid
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Email
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class EmailService(

    @Value("\${emailService.apiKey}") private val apiKey:String,
    @Value("\${emailService.fromIdentity}") private val fromIdentity:String
) {

    private val sendGrid = SendGrid(apiKey)

    fun sendTestEmail(){
        val request = Request()
        val from = Email(fromIdentity)
        val subject = "Sending with SendGrid is Fun"
        val to = Email("andreia@apadmi.com")
        val content = Content("text/plain", "and easy to do anywhere, even with Java")
        val mail = Mail(from, subject, to, content)

        request.apply {
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