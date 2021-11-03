package com.andrei.finalyearprojectapi.filters.access


import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.filters.shouldCheckFilter
import com.andrei.finalyearprojectapi.utils.JWTTokenUtility
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletRequest

import org.springframework.web.bind.annotation.RequestMethod


@SpringBootTest
class AccessTokenFilterTest{

    @Autowired
    private lateinit var accessTokenFilter: AccessTokenFilter

    @Autowired
    private lateinit var jwtTokenUtility: JWTTokenUtility

   private var mockRequest = MockHttpServletRequest(
        null,
        RequestMethod.GET.name,
        "/test"
    )

     @BeforeEach
     fun setUp(){
         accessTokenFilter= spy(accessTokenFilter)
         mockRequest =  MockHttpServletRequest()
         `when`(mockRequest.shouldCheckFilter(accessTokenFilter)).thenReturn(true)
    }


    @Test
    fun `Given no access token header the filter should not pass`(){
        assert(!accessTokenFilter.isFilterPassed(mockRequest))
    }

    @Test
    fun `Given null access token the filter should not pass`(){
        mockRequest.addHeader(JWTToken.headerName,"null")
        `when`(mockRequest.shouldCheckFilter(accessTokenFilter)).thenReturn(true)
        assert(!accessTokenFilter.isFilterPassed(mockRequest))
    }
    @Test
    fun `Given invalid access token the filter should not pass`(){
        mockRequest.addHeader(JWTToken.headerName,"Bearer sdfsdf")
        assert(!accessTokenFilter.isFilterPassed(mockRequest))
    }


    @Test
    fun `Given valid access token the filter will pass`(){
        val token = jwtTokenUtility.generateAccessToken(User()).rawValue
        mockRequest.addHeader("Authorization","Bearer $token")
        assert(accessTokenFilter.isFilterPassed(mockRequest))
    }
}
