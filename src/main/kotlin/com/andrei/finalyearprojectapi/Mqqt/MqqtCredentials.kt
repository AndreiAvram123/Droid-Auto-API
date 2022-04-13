package com.andrei.finalyearprojectapi.Mqqt

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class  MqqtCredentials(
    @Value("\${mqqt.username}")  val username:String,
    @Value("\${mqqt.password}")  val password:String,
    @Value("\${mqqt.host}")  val host:String
)