package com.andrei.finalyearprojectapi

import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.utils.JWTToken
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.mock.web.MockHttpServletRequest
import java.util.*

fun generateExpiredToken(
    user: User,
    signingKey:String
):String {
    val pastDate = Date(System.currentTimeMillis() - 10000)
    return JWT.create().withSubject(user.email)
        .withClaim(JWTToken.userIDKey, user.id)
        .withExpiresAt(pastDate).sign(Algorithm.HMAC512(signingKey.toByteArray()))
}

fun MockHttpServletRequest.setAccessToken(token:String){
    this.addHeader(JWTToken.headerName, "${JWTToken.tokenPrefix} $token")
}
