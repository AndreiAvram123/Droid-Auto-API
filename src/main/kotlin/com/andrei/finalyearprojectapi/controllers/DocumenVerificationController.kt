package com.andrei.finalyearprojectapi.controllers

import com.andrei.finalyearprojectapi.models.InquiryCompleteResponseWrapper
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.utils.ApiResponse
import com.andrei.finalyearprojectapi.utils.NoData
import com.andrei.finalyearprojectapi.utils.badRequest
import com.andrei.finalyearprojectapi.utils.nothing
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class DocumentVerificationController(
    private val userRepository: UserRepository
) {


    @PostMapping("/document/verification")
    fun confirmVerificationWebhook(
        @RequestBody
        requestBody: InquiryCompleteResponseWrapper
    ):ApiResponse<NoData>{
        val user = userRepository.findTopById(requestBody.data.attributes.payload.data.attributes.referenceId.toLong()) ?: return badRequest("Invalid user id")
        user.identityVerified = true
        return nothing()
    }

}