package com.andrei.finalyearprojectapi.utils

import io.lettuce.core.api.sync.RedisCommands

/**
 * This is stupid. Heroku puts the username for the redis but it won't work on
 * any client, we need to delete the username
 */
 fun String.withNoRedisUsername():String{
    val extractRegex = "(?<=://)[0-9a-zA-Z]+(?=:)".toRegex()
    return this.replace(extractRegex,"")
}

fun  RedisCommands<String, String>.keyExists(key:String):Boolean =  exists(key) > 0
fun RedisCommands<String, String>.hasExpireTime(key: String):Boolean = ttl(key) >0