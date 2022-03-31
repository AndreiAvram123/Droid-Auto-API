package com.andrei.finalyearprojectapi.filters.access

import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.entity.enums.UserRole
import com.andrei.finalyearprojectapi.filters.FilterDataObject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest

class AdminTokenFilterTest{

    var request = MockHttpServletRequest()

    private val userDataObject:FilterDataObject = FilterDataObject()

    private val adminTokenFilter: AdminTokenFilter = AdminTokenFilterImpl(
        useFilterDataObject = userDataObject
    )




    @AfterEach
    fun tearDown(){
        userDataObject.apply {
            user = null
        }
    }

    @Test
    fun `Given user role token the filter should not pass`(){
         userDataObject.apply {
             user = User(role = UserRole.USER)
         }
        assert(!adminTokenFilter.isFilterPassed(request))
    }
    @Test
    fun `Given token with user role  the filter should not pass`(){
        userDataObject.apply {
            user = User(role = UserRole.UNKNOWN)
        }
        assert(!adminTokenFilter.isFilterPassed(request))
    }

    @Test
    fun `Given admin role token the filter should  pass`(){
        userDataObject.apply {
            user = User(role = UserRole.ADMIN)
        }
        assert(adminTokenFilter.isFilterPassed(request))
    }
}