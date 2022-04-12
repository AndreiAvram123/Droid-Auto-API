package com.andrei.finalyearprojectapi

import com.andrei.finalyearprojectapi.configuration.beans.MqqtMessageGateway
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component


@SpringBootApplication

class FinalYearProjectApiApplication

fun main(args: Array<String>) {

    runApplication<FinalYearProjectApiApplication>(*args)
}

@Component
class AfterStartUp(
    private val messageGateway: MqqtMessageGateway
) {

    @EventListener(ApplicationReadyEvent::class)
    fun doSomethingAfterStartup() {
       messageGateway.configure()
    }
}