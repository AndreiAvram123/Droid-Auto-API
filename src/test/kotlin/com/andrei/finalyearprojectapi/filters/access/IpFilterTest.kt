package com.andrei.finalyearprojectapi.filters.access

import com.andrei.finalyearprojectapi.configuration.TestDetails
import com.andrei.finalyearprojectapi.entity.IpAddress
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.filters.FilterDataObject
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest

class IpFilterTest{

     private val userDataObject = FilterDataObject().apply {
         user = User().apply {
             ipAddresses.add(
                 IpAddress(
                 value = TestDetails.testIP
             ))
         }
     }
    private val ipFilter = IpFilter(userDataObject)

    @Test
    fun `Given unregistered ip address the filter should not pass`(){
        val mockRequest = MockHttpServletRequest().apply {
            remoteAddr = "987654321"
        }
        assert(!ipFilter.isFilterPassed(mockRequest))
    }

    @Test
    fun `Given registered ip address the filter should pass`(){
        val mockRequest = MockHttpServletRequest().apply {
            remoteAddr = TestDetails.testIP
        }
        assert(ipFilter.isFilterPassed(mockRequest))
    }
 }