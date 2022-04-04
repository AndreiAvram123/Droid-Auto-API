package com.andrei.finalyearprojectapi.utils

/**
 * This is stupid. Heroku puts the username for the redis but it won't work on
 * any client, we need to delete the username
 */
 fun String.withNoRedisUsername():String{
    val extractRegex = "(?<=://)[0-9a-zA-Z]+(?=:)".toRegex()
    return this.replace(extractRegex,"")
}