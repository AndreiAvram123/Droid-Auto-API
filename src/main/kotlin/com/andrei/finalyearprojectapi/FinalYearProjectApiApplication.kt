package com.andrei.finalyearprojectapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.time.ExperimentalTime

@SpringBootApplication
@OptIn(ExperimentalTime::class)
class FinalYearProjectApiApplication

fun main(args: Array<String>) {
    runApplication<FinalYearProjectApiApplication>(*args)
}
