package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.exceptions.RegisterException
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.request.auth.UserRegisterRequest
import com.andrei.finalyearprojectapi.request.auth.toUser
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.notAcceptable
import com.andrei.finalyearprojectapi.utils.okResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
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




    private fun isNewUserValid(user:User):Boolean{
        userRepository.findTopByUsername(user.username)?.let {
            throw RegisterException(registrationMessage = errorUsernameExists)
        }
        userRepository.findTopByEmail(user.email)?.let {
            throw RegisterException(registrationMessage = errorEmailAlreadyExists)
        }
        return true
    }


    companion object{
        const val errorUsernameExists = "Username already exists"
        const val errorEmailAlreadyExists = "Email already exists"
    }
}