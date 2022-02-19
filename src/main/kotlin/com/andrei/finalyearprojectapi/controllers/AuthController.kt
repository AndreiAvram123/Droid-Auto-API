package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.configuration.annotations.NoAuthenticationRequired
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.exceptions.RegisterException
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.request.auth.UserRegisterRequest
import com.andrei.finalyearprojectapi.request.auth.toUser
import com.andrei.finalyearprojectapi.services.EmailService
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.badRequest
import com.andrei.finalyearprojectapi.utils.okResponse
import com.sendgrid.helpers.mail.objects.Email
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
class AuthController(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val emailService: EmailService
) {

    @PostMapping("/register")
    fun register(@RequestBody
                 userRequest: UserRegisterRequest): ResponseWrapper<User> {
        val user = userRequest.toUser(passwordEncoder)
        if(isNewUserValid(user)) {
            userRepository.save(user)
        }
        return okResponse(user)
    }


    @NoAuthenticationRequired
    @GetMapping("/emailValid")
    fun checkIfEmailIsInUse(@RequestParam email:String) : ResponseWrapper<Nothing>{
         userRepository.findTopByEmail(email) ?: return okResponse()
         return badRequest(errorEmailAlreadyUsed)
    }


    @NoAuthenticationRequired
    @GetMapping("/test")
    fun test():ResponseWrapper<Nothing>{
        emailService.sendConfirmationEmail(
            to = Email("andreia@apadmi.com")
        )
        return okResponse();
    }




    private fun isNewUserValid(user:User):Boolean{
        userRepository.findTopByUsername(user.username)?.let {
            throw RegisterException(registrationMessage = errorUsernameExists)
        }
        userRepository.findTopByEmail(user.email)?.let {
            throw RegisterException(registrationMessage = errorEmailAlreadyUsed)
        }
        return true
    }


    companion object{
        const val errorUsernameExists = "Username already exists"
        const val errorEmailAlreadyUsed = "Email already used"
    }
}