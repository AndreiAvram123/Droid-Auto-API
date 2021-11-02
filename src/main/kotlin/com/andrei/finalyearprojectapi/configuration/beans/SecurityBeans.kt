package com.andrei.finalyearprojectapi.configuration.beans

import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
class SecurityBeans{
    @Bean(name = ["bCryptPasswordEncoder"])
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    fun getBCryptPasswordEncoder() = BCryptPasswordEncoder()
}