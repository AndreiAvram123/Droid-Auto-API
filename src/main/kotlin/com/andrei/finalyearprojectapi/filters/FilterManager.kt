package com.andrei.finalyearprojectapi.filters

import com.andrei.finalyearprojectapi.filters.access.AccessTokenFilter
import com.andrei.finalyearprojectapi.filters.access.AdminTokenFilter
import com.andrei.finalyearprojectapi.utils.writeJsonResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

abstract  class FilterManger : OncePerRequestFilter(){
   abstract val filters:List<SecurityFilter>
}
@Component
class FilterManagerImpl(
    accessTokenFilter: AccessTokenFilter,
    adminTokenFilter: AdminTokenFilter
) : FilterManger() {
    //the order in which filters are added matters
    override val filters: List<SecurityFilter> = listOf(
        accessTokenFilter,
    adminTokenFilter)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        var filtersPassed = true
        for(filter in filters) {
            if (request.shouldCheckFilter(filter)) {
                if (!filter.isFilterPassed(request)) {
                        response.writeJsonResponse(
                            filter.generateErrorResponse()
                        )
                        filtersPassed = false
                        break
                    }

            }
        }
        //if all filters passed continue the request flow

        if(filtersPassed) {
            filterChain.doFilter(request, response)
        }
    }



}