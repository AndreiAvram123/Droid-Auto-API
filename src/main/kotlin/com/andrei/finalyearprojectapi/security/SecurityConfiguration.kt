package com.andrei.finalyearprojectapi.security

import com.andrei.finalyearprojectapi.authentication.ApplicationUseDetailsService
import com.andrei.finalyearprojectapi.filters.FilterManger
import com.andrei.finalyearprojectapi.filters.authentication.AuthenticationFilter
import com.andrei.finalyearprojectapi.filters.exception.ExceptionHandlingFilter
import com.andrei.finalyearprojectapi.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource



@EnableWebSecurity
class SecurityConfiguration(
    private val applicationUserDetailsService: ApplicationUseDetailsService,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val filterManger: FilterManger,
    private val exceptionHandlingFilter: ExceptionHandlingFilter
) :WebSecurityConfigurerAdapter(

){
    @Bean(name = [BeanIds.AUTHENTICATION_MANAGER])
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Autowired
    @Lazy
    private lateinit var authenticationFilter: AuthenticationFilter

    override fun configure(http: HttpSecurity?) {
        http!!.cors().and().csrf().disable()
            .addFilter(authenticationFilter)
            .addFilterBefore(exceptionHandlingFilter,AuthenticationFilter::class.java)
            .addFilterAfter(filterManger,AuthenticationFilter::class.java)
            .sessionManagement()?.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

    }
    @Bean
    fun corsConfigurationSource() = UrlBasedCorsConfigurationSource().apply {
        val corsConfiguration = CorsConfiguration()
        corsConfiguration.apply {
            allowedHeaders = listOf("*")
            maxAge = 1800L
            allowedOrigins = listOf("*")
            allowedMethods = listOf(
                HttpMethod.GET.name, HttpMethod.HEAD.name, HttpMethod.POST.name, HttpMethod.PUT.name,
                HttpMethod.PATCH.name, HttpMethod.DELETE.name)
        }
        registerCorsConfiguration("/**", corsConfiguration)
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(applicationUserDetailsService)?.passwordEncoder(bCryptPasswordEncoder)
    }

}