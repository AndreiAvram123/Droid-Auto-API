package com.andrei.finalyearprojectapi

import com.andrei.finalyearprojectapi.utils.withNoRedisUsername
import org.junit.jupiter.api.Test

class RedisStringsTest {

    @Test
    fun `Given regex resulted string will not contain username`() {
       val  badConnectionString = "redis://redistogo:sdfsdfe819651f5f95bfsd3125d5b174e@porgy.redistogo.com:9569/"
        assert(badConnectionString.withNoRedisUsername() == "redis://:sdfsdfe819651f5f95bfsd3125d5b174e@porgy.redistogo.com:9569/")
    }
}