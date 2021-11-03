package com.andrei.finalyearprojectapi.filters

import com.andrei.finalyearprojectapi.utils.ResponseWrapper
import javax.servlet.http.HttpServletRequest

interface SecurityFilter {
   fun shouldCheckRequest(request: HttpServletRequest):Boolean
   fun isFilterPassed(request:HttpServletRequest):Boolean
   fun generateErrorResponse():ResponseWrapper<String>
}
fun HttpServletRequest.shouldCheckFilter(filter:SecurityFilter) = filter.shouldCheckRequest(this)