package com.andrei.finalyearprojectapi.response

data class LoginResponse(
    val emailVerified:Boolean,
    val identityVerified: Boolean,
    var accessToken:String? = null,
    var refreshToken:String? = null
)

