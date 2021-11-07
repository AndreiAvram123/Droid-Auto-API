package com.andrei.finalyearprojectapi.filters.exception

class UserDataObjectException : Exception(errorMessage){
    companion object{
        const val errorMessage = "Some values inside the user data object are still in their default state. They should not be when trying to access them"
    }
}