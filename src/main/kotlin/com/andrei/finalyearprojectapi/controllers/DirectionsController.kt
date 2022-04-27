package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.request.auth.DirectionsRequest
import com.andrei.finalyearprojectapi.response.DirectionsResponse
import com.andrei.finalyearprojectapi.services.MapsService
import com.andrei.finalyearprojectapi.utils.Controllers
import com.andrei.finalyearprojectapi.utils.ApiResponse
import com.andrei.finalyearprojectapi.utils.okResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class DirectionsController(
    private val mapsService: MapsService
):BaseRestController() {

    @GetMapping("/directions")
    fun getDirections(
        @RequestParam startLatitude:Double,
        @RequestParam startLongitude:Double,
        @RequestParam endLatitude:Double,
        @RequestParam endLongitude:Double,
    ):ApiResponse<DirectionsResponse> = mapsService.getWalkingDirections(
              DirectionsRequest(
                  startLatitude = startLatitude,
                  startLongitude = startLongitude,
                  endLatitude = endLatitude,
                  endLongitude =  endLongitude
              )
          )

    final override fun registerController() {
        Controllers.add(this::class)
    }
}