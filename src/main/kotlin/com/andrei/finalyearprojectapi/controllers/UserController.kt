package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.utils.ApiResponse
import com.andrei.finalyearprojectapi.utils.Controllers
import com.andrei.finalyearprojectapi.utils.okResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController : BaseRestController() {

    @GetMapping("/users/current")
    fun getCurrentUser(
        user: User
    ):ApiResponse<User> = okResponse(user)

    override fun registerController() {
        Controllers.add(this::class)
    }
}