package com.andrei.finalyearprojectapi.configuration.beans

import com.andrei.finalyearprojectapi.utils.withNoRedisUsername
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulRedisConnection
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Scope


@Configuration
class DatabaseModule(
      @Value("\${REDISTOGO_URL}") private val redisConnectionString: String
){

      @Bean
      @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
      fun provideRedisConnection(): StatefulRedisConnection<String, String> {
            val redisURI = RedisURI.create(redisConnectionString.withNoRedisUsername())
            redisURI.isVerifyPeer = false
            val redisClient = RedisClient.create(redisURI)
            return redisClient.connect()
      }

}