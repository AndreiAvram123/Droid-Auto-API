package com.andrei.finalyearprojectapi.exceptions

import java.lang.Exception

class InvalidJsonException : Exception(){
       val errorMessage = "Invalid json"
}