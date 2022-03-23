package com.andrei.finalyearprojectapi.request.auth

import com.andrei.finalyearprojectapi.entity.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.validation.constraints.NotBlank

data class UserRegisterRequest(
       @field:NotBlank(message = "First name should not be blank")
       val firstName:String = "",
        @field:NotBlank(message = "Last name should not be blank")
        val lastName:String = "",
       @field:NotBlank(message = "Username should not be blank")
        val username:String = "",
       @field:NotBlank(message = "Email should not be blank")
        val email :String ="",
       @field:NotBlank(message = "Email should not be blank")
       val password:String = "",

)

fun UserRegisterRequest.toUser(passwordEncoder: BCryptPasswordEncoder) = User(
                username = username,
                firstName = firstName,
                lastName = lastName,
                email =  email,
                password = passwordEncoder.encode(password)
)
