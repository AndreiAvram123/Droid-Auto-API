package com.andrei.finalyearprojectapi.configuration.beans

import org.springframework.stereotype.Component
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Component
@EnableWebMvc
class WebMvcContext (
    private val userResolver: UserResolver
) : WebMvcConfigurer{

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
       resolvers.add(userResolver)
    }
}