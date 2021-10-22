package com.andrei.finalyearprojectapi.utils

import com.andrei.finalyearprojectapi.configuration.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity


typealias ResponseWrapper<T> = ResponseEntity<ApiResponse<T>>

fun  <T> badRequest(error:String): ResponseWrapper<T> =
    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.Error(error))

fun  <T> noContent(error:String): ResponseWrapper<T> =
    ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.Error(error))

fun <T> notAcceptable(error: String):ResponseWrapper<T> =
    ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ApiResponse.Error(error))


fun <T> okResponse(data: T? = null) : ResponseWrapper<T> = ResponseEntity.ok(ApiResponse.Success(data))