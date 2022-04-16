package com.andrei.finalyearprojectapi.controllers

abstract class BaseRestController {
    init {
        registerController()
    }

   abstract fun registerController()
}