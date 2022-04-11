package com.andrei.finalyearprojectapi.controllers


import com.andrei.finalyearprojectapi.configuration.ApiResponse
import com.andrei.finalyearprojectapi.configuration.TestDetails
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.exceptions.InvalidJsonException
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.request.auth.LoginRequest
import com.google.gson.Gson
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class AuthControllerTest{

    @Autowired
    private lateinit var mockMvc: MockMvc
    private val gson = Gson()

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    @BeforeAll
    fun setUpBeforeAll(){
        val user = User(password = bCryptPasswordEncoder.encode(TestDetails.testPassword))
        userRepository.save(user)
    }


    @Test
    fun `Given null request  json the response code should be 400 and the error should be invalid json`(){
         val loginRequest = MockMvcRequestBuilders.post("/login")
         mockMvc
             .perform(loginRequest)
             .andExpect(status().isBadRequest)
             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
             .andExpect(content().json(gson.toJson(ApiResponse.Error(InvalidJsonException.errorMessage))))
    }
    @Test
    fun `Given invalid request json the response code should be 400 and the error should be invalid json`(){
         val loginRequest = MockMvcRequestBuilders.post("/login").apply {
             contentType(MediaType.APPLICATION_JSON)
             content("dfd")
         }

        mockMvc
            .perform(loginRequest)
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(gson.toJson(ApiResponse.Error(InvalidJsonException.errorMessage))))
    }

    @Test
    fun `Given valid request json but the user details invalid the response code should be 401 `(){
        val loginDetails = LoginRequest(
            username = "andrei",
            password = "andrei1223"
        )

        val loginRequest = MockMvcRequestBuilders.post("/login").apply {
            content(gson.toJson(loginDetails))
            contentType(MediaType.APPLICATION_JSON)
        }

        mockMvc
            .perform(loginRequest)
            .andExpect(status().isUnauthorized)

    }

    @Test
    fun `Given valid request json with valid user credentials the response should contain login details `(){
        val loginDetails = LoginRequest(
            username = TestDetails.testUsername,
            password = TestDetails.testPassword
        )

        val loginRequest = MockMvcRequestBuilders.post("/login").apply {
            content(gson.toJson(loginDetails))
            contentType(MediaType.APPLICATION_JSON)
        }

        mockMvc
            .perform(loginRequest)
            .andExpect(status().isOk)

    }
}