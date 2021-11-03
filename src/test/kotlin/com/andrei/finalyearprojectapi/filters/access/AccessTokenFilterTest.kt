package com.andrei.finalyearprojectapi.filters.access


import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.utils.JWTTokenUtility
import com.andrei.finalyearprojectapi.utils.notAcceptable
import org.aspectj.lang.annotation.Before
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.mockito.Spy
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

     @BeforeEach
     fun setUp(){
         accessTokenFilter= spy(accessTokenFilter)
    }


    @Test
    fun `Given null access token the filter will not pass`(){
        accessTokenFilter= spy(accessTokenFilter)
        val request = MockHttpServletRequest(
            null,
            RequestMethod.GET.name,
            "/test"
        )

        `when`(accessTokenFilter.shouldCheckFilter(request)).thenReturn(true)

        assert(!accessTokenFilter.isFilterPassed(request))
    }
    @Test
    fun `Given invalid access token the filter will not pass`(){
        accessTokenFilter= spy(accessTokenFilter)
        val request = MockHttpServletRequest(
            null,
            RequestMethod.GET.name,
            "/test"
        )
        request.apply {
            addHeader("Authorization","Bearer sdfsdf")
        }

        `when`(accessTokenFilter.shouldCheckFilter(request)).thenReturn(true)

        assert(!accessTokenFilter.isFilterPassed(request))
    }

    @Test
    fun `Given valid access token the filter will pass`(){
        accessTokenFilter= spy(accessTokenFilter)
        val request = MockHttpServletRequest(
            null,
            RequestMethod.GET.name,
            "/test"
        )
        val token = jwtTokenUtility.generateAccessToken(User()).rawValue
        request.apply {
            addHeader("Authorization","Bearer $token")
        }

        `when`(accessTokenFilter.shouldCheckFilter(request)).thenReturn(true)

        assert(accessTokenFilter.isFilterPassed(request))
    }
}
