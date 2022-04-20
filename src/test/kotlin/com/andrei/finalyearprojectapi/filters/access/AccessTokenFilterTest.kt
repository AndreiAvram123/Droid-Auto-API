package com.andrei.finalyearprojectapi.filters.access


import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.filters.FilterDataObject
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.utils.JWTUtils
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest


class AccessTokenFilterTest{



    private val jwtUtils: JWTUtils = mockk(relaxed = true)

    private val userRepository: UserRepository = mockk(relaxed = true)

    private val  accessTokenFilter: AccessTokenFilter = AccessTokenFilter(
        userRepository = userRepository,
        jwtUtils = jwtUtils,
        filterDataObject = FilterDataObject()
    )

     private var mockRequest = MockHttpServletRequest()

     private val testUser:User = mockk(relaxed = true)

     @BeforeEach
     fun setUp(){
         mockRequest =  MockHttpServletRequest()
         coEvery {
             userRepository.findTopById(testUser.id)
         }returns testUser
    }


    @Test
    fun `Given no access token header the filter should not pass`(){
        assert(!accessTokenFilter.isFilterPassed(mockRequest))
    }


    @Test
    fun `Given valid access token the filter will pass`(){

        val rawToken ="${JWTUtils.JWTToken.tokenPrefix} token"
        val jwtPayload: JWTUtils.JWTPayload = mockk(relaxed = true)

        coEvery {
            jwtUtils.parseAccessTokenPayload(rawToken)
        } returns jwtPayload



        mockRequest.addHeader(JWTUtils.JWTToken.headerName, rawToken)

        assert(accessTokenFilter.isFilterPassed(mockRequest))
    }
}
