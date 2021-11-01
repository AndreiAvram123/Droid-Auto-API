package com.andrei.finalyearprojectapi.security

import com.andrei.finalyearprojectapi.authentication.ApplicationUseDetailsService
import com.andrei.finalyearprojectapi.filters.authentication.AuthenticationFilter
import org.springframework.context.annotation.Bean
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
) :WebSecurityConfigurerAdapter(

){
    @Bean(name = [BeanIds.AUTHENTICATION_MANAGER])
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }

    override fun configure(http: HttpSecurity?) {
        http!!.cors().and().csrf().disable()
            .addFilter(AuthenticationFilter(authenticationManager()))
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