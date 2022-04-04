package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.entity.Car
import com.andrei.finalyearprojectapi.repositories.CarRepository
import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import com.andrei.finalyearprojectapi.utils.okResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CarController(
    private val carRepository: CarRepository
) {

    @GetMapping("/nearby")
    fun getNearbyCars():ResponseWrapper<List<Car>>{
        return okResponse(
            carRepository.findAll()
        )
    }

}