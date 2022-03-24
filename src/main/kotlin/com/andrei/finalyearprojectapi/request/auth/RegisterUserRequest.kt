package com.andrei.finalyearprojectapi.request.auth

import com.andrei.finalyearprojectapi.entity.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.validation.constraints.NotBlank

data class RegisterUserRequest(
       @field:NotBlank(message = "First name should not be blank")
       val firstName:String = "",
        @field:NotBlank(message = "Last name should not be blank")
        val lastName:String = "",
       @field:NotBlank(message = "Email should not be blank")
        val email :String ="",
       @field:NotBlank(message = "Email should not be blank")
       val password:String = "",

)

fun RegisterUserRequest.toUser(passwordEncoder: BCryptPasswordEncoder) = User(
                firstName = firstName,
                lastName = lastName,
                email =  email,
                password = passwordEncoder.encode(password)
)
