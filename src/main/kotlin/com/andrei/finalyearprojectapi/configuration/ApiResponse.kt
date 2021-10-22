package com.andrei.finalyearprojectapi.configuration

sealed class ApiResponse<out T>(val isSuccessful:Boolean){
    data class Success<T>(val data : T? = null) : ApiResponse<T>(true)
    data class Error(val error: String): ApiResponse<Nothing>(false)
}
