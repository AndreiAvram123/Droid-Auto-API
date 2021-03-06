package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.configuration.annotations.NoAuthenticationRequired
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.exceptions.RegisterException
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.request.auth.NewTokenRequest
import com.andrei.finalyearprojectapi.request.auth.RegisterUserRequest
import com.andrei.finalyearprojectapi.request.auth.toUser
import com.andrei.finalyearprojectapi.response.TokenResponse
import com.andrei.finalyearprojectapi.services.EmailService
import com.andrei.finalyearprojectapi.utils.*
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
) :BaseRestController(){


    @PostMapping("/register")
    @NoAuthenticationRequired
    fun register(@RequestBody
                 @Valid
                 userRequest: RegisterUserRequest
    ): ApiResponse<User> {

        val user = userRequest.toUser(passwordEncoder)

        if(isNewUserValid(user)) {
            userRepository.save(user)
        }
        //send a verification email after successfully registered
        emailService.sendVerificationEmail(
            to = Email(userRequest.email)
        )
        return createdResponse(user)
    }

    @NoAuthenticationRequired
    @GetMapping("/email/valid")
    fun checkIfEmailIsInUse(@RequestParam email:String) : ApiResponse<NoData>{
         userRepository.findTopByEmail(email) ?: return nothing()
         return notAcceptable(errorEmailAlreadyUsed)
    }


    @PostMapping("/email/verification")
    fun sendVerificationEmail(
        user:User
    ):ApiResponse<NoData> = emailService.sendVerificationEmail(
           to =  Email(user.email)
        )

    @PostMapping("/token")
    @NoAuthenticationRequired
    fun getNewAccessToken(
        @RequestBody
        @Valid
        newTokenRequest: NewTokenRequest
    ):ApiResponse<TokenResponse>{

        val payload = jwtUtils.parseRefreshTokenPayload(newTokenRequest.refreshToken).getOrNull() ?: return badRequest(
        "Not a valid refresh token"
        )

        val user = userRepository.findTopById(payload.userID) ?: return badRequest("The refresh token does not belong to a valid user")
        return okResponse(
            TokenResponse(
                accessToken = jwtUtils.generateAccessToken(user)
            )
        )
    }


    @GetMapping("/email/verification")
    @NoAuthenticationRequired
    private fun verifyEmail(
        @RequestParam uuid:String
    ):ApiResponse<NoData>{
        //todo
        //should check
         userRepository.findTopByEmail(uuid)?.apply {
            emailVerified = true
        }?.also {
            userRepository.save(it)
         }

        return nothing()
    }



    @Throws(RegisterException::class)
    private fun isNewUserValid(user:User):Boolean{
        userRepository.findTopByEmail(user.email)?.let {
            throw RegisterException(error = errorEmailAlreadyUsed)
        }

        userRepository.findTopByFirstNameAndLastName(
            firstName = user.firstName,
            lastName = user.lastName
        )?.let {
            throw RegisterException(error = errorNameUsed)
        }

        return true
    }


    companion object{
        const val errorEmailAlreadyUsed = "Email already used"
        const val errorNameUsed = "An user with this name already exists"
    }

    final override fun registerController() {
         Controllers.add(this::class)
    }
}