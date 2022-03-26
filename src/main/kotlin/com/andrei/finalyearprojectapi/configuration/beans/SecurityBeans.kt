package com.andrei.finalyearprojectapi.configuration.beans

import com.andrei.finalyearprojectapi.filters.FilterDataObject
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.context.annotation.RequestScope

@Configuration
class SecurityBeans{
    @Bean(name = ["bCryptPasswordEncoder"])
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun getBCryptPasswordEncoder() = BCryptPasswordEncoder()


    @RequestScope
    @Bean
    fun provideFilterDataObject(): FilterDataObject = FilterDataObject()
}