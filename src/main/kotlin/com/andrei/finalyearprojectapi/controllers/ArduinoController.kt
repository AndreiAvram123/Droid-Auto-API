package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.configuration.annotations.NoAuthenticationRequired
import com.andrei.finalyearprojectapi.configuration.beans.MqqtMessageGateway
import com.andrei.finalyearprojectapi.utils.Controllers
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ArduinoController (
     private val messageGateway: MqqtMessageGateway
): Controller() {

    init {
        registerController()
    }

    @GetMapping("/mqqt")
    @NoAuthenticationRequired
    fun test(){
      messageGateway.sendMockMessage()
    }

    final override fun registerController() {
        Controllers.add(this::class)
    }
}