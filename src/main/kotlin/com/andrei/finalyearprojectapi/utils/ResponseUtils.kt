package com.andrei.finalyearprojectapi.utils

import com.andrei.finalyearprojectapi.configuration.ApiResponse
import com.google.gson.Gson

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import javax.servlet.http.HttpServletResponse


typealias ResponseWrapper<T> = ResponseEntity<ApiResponse<T>>

fun  <T> badRequest(error:String): ResponseWrapper<T> =
    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.Error(error))

fun  <T> noContent(error:String): ResponseWrapper<T> =
    ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.Error(error))

fun <T> notAcceptable(error: String):ResponseWrapper<T> =
    ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ApiResponse.Error(error))


fun <T> okResponse(data: T? = null) : ResponseWrapper<T> = ResponseEntity.ok(ApiResponse.Success(data))

/**
 * Helper method to write a json object into
 * the servlet response
 */
 inline fun <reified T>HttpServletResponse.writeJsonResponse(response: ResponseWrapper<T>){
    val gson = Gson()
    this.writer.write(gson.toJson(response.body))
    this.contentType = MediaType.APPLICATION_JSON_VALUE
    this.status = response.statusCode.value()
}