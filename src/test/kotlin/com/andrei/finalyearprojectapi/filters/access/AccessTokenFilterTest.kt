package com.andrei.finalyearprojectapi.filters.access


import com.andrei.finalyearprojectapi.configuration.BaseIntegrationTest
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.filters.FilterDataObject
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.utils.JWTToken
import com.andrei.finalyearprojectapi.utils.JWTUtils
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockHttpServletRequest


class AccessTokenFilterTest : BaseIntegrationTest() {


    @Autowired
    protected lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var jwtUtils: JWTUtils

    private lateinit var  sut: AccessTokenFilter

     private var mockRequest = MockHttpServletRequest()

     private val testUser:User = mockk()


     @BeforeEach
     fun beforeEach(){
         mockRequest =  MockHttpServletRequest()

         every {
             testUser.id
         } returns  10

         every {
             testUser.email
         } returns "andrei@gmail.com"

         every {
             userRepository.findTopById(testUser.id)
         } returns testUser

         sut = AccessTokenFilter(
             userRepository = userRepository,
             jwtUtils = jwtUtils,
             filterDataObject = FilterDataObject()
         )
    }


    @Test
    fun `Given no access token header the filter should not pass`(){
        assert(!sut.isFilterPassed(mockRequest))
    }


    @Test
    fun `Given null or empty access token in the request header, the filter should not pass`(){
        mockRequest.addHeader(JWTToken.headerName, "")
        assert(!sut.isFilterPassed(mockRequest))
    }


}
