package com.andrei.finalyearprojectapi.filters.access

import com.andrei.finalyearprojectapi.entity.enums.UserRole
import com.andrei.finalyearprojectapi.filters.FilterDataObject
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest

class AdminTokenFilterTest{

    var request = MockHttpServletRequest()

    private val filterDataObject:FilterDataObject = mockk(relaxed = true)

    private val adminTokenFilter: AdminTokenFilter = AdminTokenFilterImpl(
        useFilterDataObject = filterDataObject
    )



    @Test
    fun `Given user role token the filter should not pass`(){
        coEvery {
            filterDataObject.getUserNotNull().role
        }returns UserRole.ADMIN

        assert(!adminTokenFilter.isFilterPassed(request))
    }
    @Test
    fun `Given token with user role  the filter should not pass`(){
        coEvery {
            filterDataObject.getUserNotNull().role
        }returns UserRole.UNKNOWN

        assert(!adminTokenFilter.isFilterPassed(request))
    }

    @Test
    fun `Given admin role token the filter should  pass`(){
        coEvery {
            filterDataObject.getUserNotNull().role
        }returns UserRole.ADMIN

        assert(adminTokenFilter.isFilterPassed(request))
    }
}