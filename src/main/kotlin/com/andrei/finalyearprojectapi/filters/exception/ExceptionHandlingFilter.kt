package com.andrei.finalyearprojectapi.filters.exception

import com.andrei.finalyearprojectapi.configuration.exception.ExceptionHandler
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.lang.Exception
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ExceptionHandlingFilter(
    private val exceptionHandler: ExceptionHandler
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try{
            filterChain.doFilter(request,response)
        }catch (e:Exception){
            exceptionHandler.handleConflict<Nothing>(
                exception = e,
                request = request,
                response = response
            )
        }
    }
}