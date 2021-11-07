package com.andrei.finalyearprojectapi.filters.access


import JWTToken
import com.andrei.finalyearprojectapi.configuration.TestDetails
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.utils.JWTTokenUtility
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.mock.web.MockHttpServletRequest


@SpringBootTest
class AccessTokenFilterTest{

    @Autowired
    private lateinit var accessTokenFilter: AccessTokenFilter

    @Autowired
    private lateinit var jwtTokenUtility: JWTTokenUtility


    @MockBean
    private lateinit var userRepository: UserRepository

     private var mockRequest = MockHttpServletRequest()

     private val testUser = User(id = TestDetails.testId)

     @BeforeEach
     fun setUp(){
         mockRequest =  MockHttpServletRequest()
         `when`(userRepository.findTopById(testUser.id)).thenReturn(testUser)
    }


    @Test
    fun `Given no access token header the filter should not pass`(){
        assert(!accessTokenFilter.isFilterPassed(mockRequest))
    }

    @Test
    fun `Given null access token the filter should not pass`(){
        mockRequest.addHeader(JWTToken.headerName,"null")
        assert(!accessTokenFilter.isFilterPassed(mockRequest))
    }
    @Test
    fun `Given invalid access token the filter should not pass`(){
        mockRequest.addHeader(JWTToken.headerName,"Bearer sdfsdf")
        assert(!accessTokenFilter.isFilterPassed(mockRequest))
    }


    @Test
    fun `Given valid access token the filter will pass`(){
        val token = jwtTokenUtility.generateAccessToken(testUser).rawValue
        mockRequest.addHeader("Authorization","Bearer $token")
        assert(accessTokenFilter.isFilterPassed(mockRequest))
    }
}
