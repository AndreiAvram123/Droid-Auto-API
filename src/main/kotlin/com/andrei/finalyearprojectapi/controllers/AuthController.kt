package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.configuration.annotations.NoAuthenticationRequired
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.exceptions.RegisterException
import com.andrei.finalyearprojectapi.filters.RequestDataObject
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.request.auth.RegisterUserRequest
import com.andrei.finalyearprojectapi.request.auth.toUser
import com.andrei.finalyearprojectapi.services.EmailService
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.badRequest
import com.andrei.finalyearprojectapi.utils.okResponse
import com.sendgrid.helpers.mail.objects.Email
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
class AuthController(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val emailService: EmailService,
    private var requestDataObject: RequestDataObject
) {

    @PostMapping("/register")
    @NoAuthenticationRequired
    @Throws(RegisterException::class)
    fun register(@RequestBody
                 @Valid
                 userRequest: RegisterUserRequest): ResponseWrapper<User> {
        val user = userRequest.toUser(passwordEncoder)
        if(isNewUserValid(user)) {
            userRepository.save(user)
        }
        //send a verification email after successfully registered
        emailService.sendVerificationEmail(
            to = Email(userRequest.email)
        )
        return okResponse(user)
    }

    @NoAuthenticationRequired
    @GetMapping("/emailValid")
    fun checkIfEmailIsInUse(@RequestParam email:String) : ResponseWrapper<Nothing>{
         userRepository.findTopByEmail(email) ?: return okResponse()
         return badRequest(errorEmailAlreadyUsed)
    }


    @PostMapping("/email/verification")
    fun sendVerificationEmail():ResponseWrapper<Nothing>{
        val user = requestDataObject.getUserNotNull();
        emailService.sendVerificationEmail(
           to =  Email(user.email)
        )
        return okResponse();
    }




    @Throws(RegisterException::class)
    private fun isNewUserValid(user:User):Boolean{
        userRepository.findTopByEmail(user.email)?.let {
            throw RegisterException(registrationMessage = errorEmailAlreadyUsed)
        }

        return true
    }


    companion object{
        const val errorEmailAlreadyUsed = "Email already used"
    }
}