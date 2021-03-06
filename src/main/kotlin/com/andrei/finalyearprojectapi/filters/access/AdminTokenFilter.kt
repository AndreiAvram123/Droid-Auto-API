package com.andrei.finalyearprojectapi.filters.access

import com.andrei.finalyearprojectapi.configuration.annotations.AdminTokenRequired
import com.andrei.finalyearprojectapi.entity.enums.UserRole
import com.andrei.finalyearprojectapi.filters.FilterDataObject
import com.andrei.finalyearprojectapi.filters.SecurityFilter
import com.andrei.finalyearprojectapi.utils.ApiResponse
import com.andrei.finalyearprojectapi.utils.endpointHasAnnotation
import com.andrei.finalyearprojectapi.utils.notAuthorized
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

abstract class AdminTokenFilter : SecurityFilter

/**
 * Filter used in order to determine if the user is authorized to access
 * the specific admin resource
 *
 */
@Component
class AdminTokenFilterImpl(
    private var useFilterDataObject: FilterDataObject
) :AdminTokenFilter() {

    override fun shouldCheckRequest(request: HttpServletRequest): Boolean  = request.endpointHasAnnotation<AdminTokenRequired>()

    override fun isFilterPassed(request: HttpServletRequest): Boolean {
        return  useFilterDataObject.getUserNotNull().role == UserRole.ADMIN
    }

    override fun generateErrorResponse(): ApiResponse<String>  = notAuthorized()

}
