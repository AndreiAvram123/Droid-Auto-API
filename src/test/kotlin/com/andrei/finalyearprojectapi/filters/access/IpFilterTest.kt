package com.andrei.finalyearprojectapi.filters.access

import com.andrei.finalyearprojectapi.configuration.TestData
import com.andrei.finalyearprojectapi.entity.IpAddress
import com.andrei.finalyearprojectapi.filters.FilterDataObject
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest

class IpFilterTest{

     private val filterDataObject:FilterDataObject = mockk()

    private val ipFilter = IpFilter(filterDataObject)

    @Test
    fun `Given unregistered ip address the filter should not pass`(){
        val testIP = "987654321"
        val mockRequest = MockHttpServletRequest().apply {
            remoteAddr = testIP
        }
        coEvery {
            filterDataObject.getUserNotNull().ipAddresses
        }returns mutableListOf(IpAddress(
            id = 1,
            value = testIP
        ))
        assert(!ipFilter.isFilterPassed(mockRequest))
    }

    @Test
    fun `Given registered ip address the filter should pass`(){
        val mockRequest = MockHttpServletRequest().apply {
            remoteAddr = TestData.testIP
        }
        assert(ipFilter.isFilterPassed(mockRequest))
    }
 }