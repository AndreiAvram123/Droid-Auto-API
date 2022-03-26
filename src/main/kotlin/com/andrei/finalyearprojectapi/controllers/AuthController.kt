package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.configuration.annotations.NoAuthenticationRequired
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.exceptions.RegisterException
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.request.auth.RegisterUserRequest
import com.andrei.finalyearprojectapi.request.auth.toUser
import com.andrei.finalyearprojectapi.response.TokenResponse
import com.andrei.finalyearprojectapi.services.EmailService
import com.andrei.finalyearprojectapi.utils.JWTUtils
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
    private val jwtUtils: JWTUtils
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
    fun sendVerificationEmail(
        user:User
    ):ResponseWrapper<Nothing>{
        emailService.sendVerificationEmail(
           to =  Email(user.email)
        )
        return okResponse();
    }

    @GetMapping("/token")
    fun getNewAccessToken(
        @RequestParam refreshToken:String
    ):ResponseWrapper<TokenResponse>{
        val decodedRefreshToken = jwtUtils.decodeRefreshToken(refreshToken)
        if(decodedRefreshToken.userID == null){
            return badRequest(
                "Not a valid refresh token"
            )
        }
        val user = userRepository.findTopById(decodedRefreshToken.userID) ?: return badRequest("The refresh token does not belong to a valid user")
        return okResponse(
            TokenResponse(
                accessToken = jwtUtils.generateAccessToken(user).value
            )
        )
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