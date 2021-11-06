package com.andrei.finalyearprojectapi.exceptions

import java.lang.Exception

class InvalidJsonException : Exception(){
       companion object {
              const val errorMessage = "Invalid json"
       }
}