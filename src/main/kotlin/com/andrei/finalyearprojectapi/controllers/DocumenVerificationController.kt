package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.configuration.annotations.NoAuthenticationRequired
import com.andrei.finalyearprojectapi.models.InquiryCompleteResponseWrapper
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.utils.*
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DocumentVerificationController(
    private val userRepository: UserRepository
):BaseRestController() {


    @PostMapping("/document/verification")
    @NoAuthenticationRequired
    fun confirmVerificationWebhook(
        @RequestBody
        requestBody: InquiryCompleteResponseWrapper
    ):ApiResponse<NoData>{
        val user = userRepository.findTopById(requestBody.data.attributes.payload.data.attributes.referenceId.toLong()) ?: return badRequest("Invalid user id")
        user.identityVerified = true
        return nothing()
    }

    override fun registerController() {
         Controllers.add(this::class)
    }

}