package com.andrei.finalyearprojectapi.configuration.beans

import com.andrei.finalyearprojectapi.filters.UserDataObject
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.annotation.RequestScope


@Configuration
class RequestBeans {
    @Bean
    @RequestScope
    fun provideUserForRequest():UserDataObject {
        return UserDataObject()
    }
}