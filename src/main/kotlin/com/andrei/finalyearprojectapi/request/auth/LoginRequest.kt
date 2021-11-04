package com.andrei.finalyearprojectapi.request.auth

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


data class LoginRequest(
    @field:NotNull
    @field:NotBlank
    val username:String,
    @field:NotNull
    @field:NotBlank
    val password:String
)
