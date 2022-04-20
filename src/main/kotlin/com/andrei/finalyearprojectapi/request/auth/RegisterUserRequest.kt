package com.andrei.finalyearprojectapi.request.auth

import com.andrei.finalyearprojectapi.entity.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.validation.constraints.NotBlank

data class RegisterUserRequest(
       @field:NotBlank(message = RegisterUserRequestErrors.blankFirstName)
       val firstName:String ,
        @field:NotBlank(message = RegisterUserRequestErrors.blankLastName)
        val lastName:String ,
       @field:NotBlank(message = RegisterUserRequestErrors.blankEmail)
        val email :String,
       @field:NotBlank(message = RegisterUserRequestErrors.blankPassword)
       val password:String
)

object RegisterUserRequestErrors{
    const val blankEmail =  "First name should not be blank"
    const val blankFirstName =  "Last name should not be blank"
    const val blankLastName =  "Email should not be blank"
    const val blankPassword =  "password should not be blank"
}

fun RegisterUserRequest.toUser(passwordEncoder: BCryptPasswordEncoder) = User(
                id = 1,
                firstName = firstName,
                lastName = lastName,
                email =  email,
                password = passwordEncoder.encode(password)
)

