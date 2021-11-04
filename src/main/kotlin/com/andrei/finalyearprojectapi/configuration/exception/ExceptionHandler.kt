package com.andrei.finalyearprojectapi.configuration.exception

import com.andrei.finalyearprojectapi.configuration.ApiResponse
import com.andrei.finalyearprojectapi.exceptions.InvalidJsonException
import com.andrei.finalyearprojectapi.exceptions.RegisterException
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.badRequest
import com.andrei.finalyearprojectapi.utils.okResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Class responsible for global handling of exceptions
 */
@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler(){

   @ExceptionHandler
    fun <T> handleConflict(exception: Exception, request:HttpServletRequest, response: HttpServletResponse){
         when(exception){
             is RegisterException ->{
                 response.sendError(HttpStatus.CONFLICT.value(),exception.registrationMessage)
             }
             is InvalidJsonException -> {
                  response.sendError(HttpStatus.BAD_REQUEST.value(),exception.errorMessage)
             }

         }
   }
}