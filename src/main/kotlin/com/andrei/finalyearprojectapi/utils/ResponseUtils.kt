package com.andrei.finalyearprojectapi.utils

import com.andrei.finalyearprojectapi.configuration.Response
import com.google.gson.Gson
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import javax.servlet.http.HttpServletResponse



typealias ApiResponse<T> = ResponseEntity<Response<T>>

typealias NoData = Nothing?

fun  <T> badRequest(error:String): ApiResponse<T> =
    ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.Error(error))

fun  <T> noContent(error:String): ApiResponse<T> =
    ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.Error(error))

fun <T> notAcceptable(error: String):ApiResponse<T> =
    ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(Response.Error(error))

fun <T> errorResponse(code:HttpStatus, error:String):ApiResponse<T> =
    ResponseEntity.status(code).body(Response.Error(error))

fun <T> okResponse(data: T ) : ApiResponse<T> = ResponseEntity.ok(Response.Success(data))

fun nothing():ApiResponse<NoData> = ResponseEntity.ok(Response.Success(null))


fun <T> notAuthorized():ApiResponse<T> =
    ResponseEntity.status(HttpStatus.FORBIDDEN).body(Response.Error(ResponseMessages.errorNotAuthorized))

fun <T> newLoginDevice():ApiResponse<T> =
    ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(Response.Error(ResponseMessages.errorNotAuthorized))

fun <T> notAuthenticated():ApiResponse<T> =
    ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Response.Error(ResponseMessages.errorNotAuthenticated))


/**
 * Helper method to write a json object into
 * the servlet response
 */
 inline fun <reified T>HttpServletResponse.writeJsonResponse(response: ApiResponse<T>){
    val gson = Gson()
    apply {
        writer.write(gson.toJson(response.body))
        contentType = MediaType.APPLICATION_JSON_VALUE
        status = response.statusCode.value()
    }
}