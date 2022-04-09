package com.andrei.finalyearprojectapi.configuration


sealed class Response<out T>(val isSuccessful:Boolean){
    data class Success<T>(val data : T? = null) : Response<T>(true)
    data class Error(val error: String): Response<Nothing>(false)
}
