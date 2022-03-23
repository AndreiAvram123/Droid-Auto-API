package com.andrei.finalyearprojectapi.response

data class LoginResponse(
    val isEmailVerified:Boolean,
    var accessToken:String? = null,
    var refreshToken:String? = null
)

