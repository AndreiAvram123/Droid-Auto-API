package com.andrei.finalyearprojectapi.request.auth

import com.andrei.finalyearprojectapi.entity.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

data class UserRegisterRequest(
        val username:String,
        val email :String,
        val password:String
)

fun UserRegisterRequest.toUser(passwordEncoder: BCryptPasswordEncoder) = User(username = username,
                email =  email,
                password = passwordEncoder.encode(password))
