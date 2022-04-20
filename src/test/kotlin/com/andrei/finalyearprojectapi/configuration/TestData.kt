package com.andrei.finalyearprojectapi.configuration

import com.andrei.finalyearprojectapi.entity.User

object TestData{

    fun getUser(encryptedPassword:String):User = User(
        id = testId,
        firstName = firstName,
        lastName = lastName,
        email = email,
        password = encryptedPassword,
    )

    const val testId = 1L
    const val firstName = "Andrei"
    const val lastName = "Avram"
    const val email = "andrei@gmail.com"

    const val password = "andrei1239"
    const val testIP = "123456789"
}