package com.andrei.finalyearprojectapi.configuration.exception

import com.andrei.finalyearprojectapi.configuration.Response
import com.andrei.finalyearprojectapi.exceptions.InvalidJsonException
import com.andrei.finalyearprojectapi.exceptions.RegisterException
import com.andrei.finalyearprojectapi.utils.badRequest
import com.andrei.finalyearprojectapi.utils.writeJsonResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * Class responsible for global handling of exceptions
 */
@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler(){

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
       return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.Error(ex.firstError()))
    }

    @ExceptionHandler
     fun <T> handleConflict(exception: Exception, request:HttpServletRequest, response: HttpServletResponse){
         when(exception){
             is RegisterException ->{
                 response.writeJsonResponse<String>(
                     badRequest(
                         exception.registrationMessage
                     )
                 )
             }
             is InvalidJsonException -> {
                 response.writeJsonResponse<String>(
                     badRequest(
                         InvalidJsonException.errorMessage
                     )
                 )
             }
             else -> response.writeJsonResponse<String>(
                 badRequest(
                     exception.message ?: "Unknown error"
                 )
             )

         }
   }
}


private fun MethodArgumentNotValidException.firstError():String
{
   return  bindingResult.allErrors.firstOrNull()?.defaultMessage ?: "Bad data sent"
}
