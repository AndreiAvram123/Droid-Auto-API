package com.andrei.finalyearprojectapi.controllers


import com.andrei.finalyearprojectapi.configuration.BaseIntegrationTest
import com.andrei.finalyearprojectapi.configuration.Response
import com.andrei.finalyearprojectapi.configuration.TestData
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.exceptions.InvalidJsonException
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.request.auth.LoginRequest
import com.andrei.finalyearprojectapi.request.auth.RegisterUserRequest
import com.andrei.finalyearprojectapi.request.auth.RegisterUserRequestErrors
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
class AuthControllerTest : BaseIntegrationTest(){


    @MockBean
    lateinit var userRepository: UserRepository


    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var gson:Gson

    @Autowired
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    private lateinit var testUser:User


    @BeforeEach
    fun setUpBeforeAll() {
         testUser = TestData.getUser(
            bCryptPasswordEncoder.encode(TestData.password)
        )

        userRepository = mockk(relaxed = true)

        every {
            userRepository.findTopById(testUser.id)
        } returns testUser

        every {
            userRepository.findTopByEmail(testUser.email)
        } returns testUser


    }


    @Test
    fun `Given null request  json the response code should be 400 and the error should be invalid json`(){
         val loginRequest = MockMvcRequestBuilders.post("/login").apply {
             contentType(MediaType.APPLICATION_JSON)
         }

        mockMvc
             .perform(loginRequest)
             .andExpect(status().isBadRequest)
             .andExpect(content().contentType(MediaType.APPLICATION_JSON))
             .andExpect(content().json(gson.toJson(Response.Error(InvalidJsonException.errorMessage))))
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
            .andExpect(content().json(gson.toJson(Response.Error(InvalidJsonException.errorMessage))))
    }

    @Test
    fun `Given valid request json but the user details invalid the response code should be 401 `(){
        val loginDetails = LoginRequest(
            email = "andrei",
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
    fun `Given null request json, the register endpoint should return 400 and the message Invalid json `(){
        val registerRequest = MockMvcRequestBuilders.post("/register").apply {
            contentType(MediaType.APPLICATION_JSON)
        }
        //todo
        //ask on stack overflow
         mockMvc.perform(registerRequest)
             .andDo(MockMvcResultHandlers.print())
             .andExpect(status().isBadRequest)

    }

    @Test
    fun `Given invalid json, the register endpoint should return 400 and message Invalid json`(){
        val registerRequest = MockMvcRequestBuilders.post("/register").apply {
            contentType(MediaType.APPLICATION_JSON)
            content("sdfsdf")
        }
        mockMvc.perform(registerRequest)
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `Given json not containing first name , the register endpoint will return 400 and specific error message`(){
        val registerData = RegisterUserRequest(
            firstName = "",
            lastName = "Andrei",
            email = "andrei@gmail.com",
            password = "123456"
        )
        val expectedResponse = gson.toJson(
             Response.Error(
                RegisterUserRequestErrors.blankFirstName
            )
        )
        val registerRequest = MockMvcRequestBuilders.post("/register").apply {
            contentType(MediaType.APPLICATION_JSON)
            content(gson.toJson(registerData))
        }
        mockMvc.perform(registerRequest)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest)
            .andExpect(content().string(expectedResponse))
    }
    @Test
    fun `Given json not containing last name , the register endpoint will return 400 and error message `(){
        val registerData = RegisterUserRequest(
            firstName = "Andrei",
            lastName = "",
            email = "andrei@gmail.com",
            password = "123456"
        )
        val expectedResponse = gson.toJson(
            Response.Error(
                RegisterUserRequestErrors.blankLastName
            )
        )

        val registerRequest = MockMvcRequestBuilders.post("/register").apply {
            contentType(MediaType.APPLICATION_JSON)
            content(gson.toJson(registerData))
        }
        mockMvc.perform(registerRequest)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest)
            .andExpect(content().string(expectedResponse))
    }


    @Test
    fun `Given json not containing email , the register endpoint will return 400 and error message `(){
        val registerData = RegisterUserRequest(
            firstName = "Andrei",
            lastName = "Avram",
            email = "",
            password = "123456"
        )
        val expectedResponse = gson.toJson(
            Response.Error(
                RegisterUserRequestErrors.blankEmail
            )
        )
        val registerRequest = MockMvcRequestBuilders.post("/register").apply {
            contentType(MediaType.APPLICATION_JSON)
            content(gson.toJson(registerData))
        }
        mockMvc.perform(registerRequest)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest)
            .andExpect(content().string(expectedResponse))
    }
    @Test
    fun `Given json not containing password , the register endpoint will return 400 and error message `(){
        val registerData = RegisterUserRequest(
            firstName = "Andrei",
            lastName = "Avram",
            email = "andrei@gmail.com",
            password = ""
        )
        val expectedResponse = gson.toJson(
            Response.Error(
                RegisterUserRequestErrors.blankPassword
            )
        )
        val registerRequest = MockMvcRequestBuilders.post("/register").apply {
            contentType(MediaType.APPLICATION_JSON)
            content(gson.toJson(registerData))
        }
        mockMvc.perform(registerRequest)
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest)
            .andExpect(content().string(expectedResponse))
    }
    @Test
    fun `Given json containing all register details , the register endpoint will return success `() {
//        val registerData = RegisterUserRequest(
//            firstName = test
//        )
//        val registerRequest = MockMvcRequestBuilders.post("/register").apply {
//            contentType(MediaType.APPLICATION_JSON)
//            content(gson.toJson(registerData))
//        }
//        mockMvc.perform(registerRequest)
//            .andExpect(status().isCreated)

    }


}