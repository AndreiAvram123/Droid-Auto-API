package com.andrei.finalyearprojectapi.controllers

abstract class Controller {
    init {
        registerController()
    }

   abstract fun registerController()
}