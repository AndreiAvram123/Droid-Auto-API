package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.okResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CarController {

    @GetMapping("/nearby")
    fun getNearbyCars():ResponseWrapper<String>{
        return okResponse("something")
    }
}