package com.example.demo.configuration

import javax.servlet.http.HttpServletRequest

fun HttpServletRequest.getForwardedHeader():String?{
    val headerName = "X-FORWARDED-FOR"
    return this.getHeader(headerName)
}