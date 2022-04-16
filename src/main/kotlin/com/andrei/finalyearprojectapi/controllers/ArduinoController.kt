package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.Mqqt.MqqtMessageGateway
import com.andrei.finalyearprojectapi.utils.Controllers
import org.springframework.web.bind.annotation.RestController

@RestController
class ArduinoController (
     private val messageGateway: MqqtMessageGateway
): BaseRestController() {


    final override fun registerController() {
        Controllers.add(this::class)
    }
}