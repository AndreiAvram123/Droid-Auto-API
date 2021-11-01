package com.andrei.finalyearprojectapi.utils

import org.springframework.http.HttpStatus
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.http.HttpServletResponse

fun HttpServletResponse.sendNotAuthenticated(){
    sendError(401)
}
fun HttpServletResponse.sendNotAuthorised(){
    sendError(403)
}
fun HttpServletResponse.sendTooManyRequests(){
    sendError(HttpStatus.TOO_MANY_REQUESTS.value())
}
fun HttpServletResponse.sendRequireValidateIP(){
    sendError(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED.value())
}