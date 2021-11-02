package com.andrei.finalyearprojectapi.configuration.exception

import com.andrei.finalyearprojectapi.configuration.ApiResponse
import com.andrei.finalyearprojectapi.controllers.AuthController
import com.andrei.finalyearprojectapi.exceptions.RegisterException
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.notAcceptable
import com.andrei.finalyearprojectapi.utils.okResponse
import com.andrei.finalyearprojectapi.utils.writeJsonResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
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
   protected fun <T> handleConflict(exception: Exception, request:HttpServletRequest, response: HttpServletResponse):ResponseWrapper<T>{
         when(exception){
             is RegisterException ->{
                     return userExists(
                         exception.registrationMessage
                     )
             }
         }
       return okResponse()
   }

  private  fun <T> userExists(error:String) : ResponseWrapper<T> = ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.Error(error))

}