package com.andrei.finalyearprojectapi.filters.access

import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.enums.UserRole
import com.andrei.finalyearprojectapi.filters.shouldCheckFilter
import com.andrei.finalyearprojectapi.repositories.UserRepository
import com.andrei.finalyearprojectapi.utils.JWTTokenUtility
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.AutoConfigurationPackage
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.test.context.web.WebAppConfiguration

@SpringBootTest
class AdminTokenFilterTest{

    var request = MockHttpServletRequest()

    @Autowired
    private lateinit var adminTokenFilter: AdminTokenFilter

    @Autowired
    private lateinit var jwtTokenUtility: JWTTokenUtility


    @BeforeEach
    fun setUp(){
        request = MockHttpServletRequest()
        adminTokenFilter = spy(adminTokenFilter)
        `when`(request.shouldCheckFilter(adminTokenFilter)).thenReturn(true)
    }

    @Test
    fun `Given user role token the filter should not pass`(){
        val user = User(role = UserRole.USER)
        val token = jwtTokenUtility.generateAccessToken(user).rawValue
        request.addHeader(JWTToken.headerName,token)
        assert(!adminTokenFilter.isFilterPassed(request))
    }
    @Test
    fun `Given unknown role token the filter should not pass`(){
        val user = User(role = UserRole.UNKNOWN)
        val token = jwtTokenUtility.generateAccessToken(user).rawValue
        request.addHeader(JWTToken.headerName,token)
        assert(!adminTokenFilter.isFilterPassed(request))
    }

    @Test
    fun `Given admin role token the filter should  pass`(){
        val user = User(id = 1, role = UserRole.ADMIN)
        val token = jwtTokenUtility.generateAccessToken(user).rawValue
        request.addHeader(JWTToken.headerName,token)
        assert(adminTokenFilter.isFilterPassed(request))
    }
}