package com.andrei.finalyearprojectapi.services

import com.andrei.finalyearprojectapi.Mqqt.commands.LockCarCommand
import com.andrei.finalyearprojectapi.Mqqt.commands.UnlockCarCommand
import com.andrei.finalyearprojectapi.entity.Car
import org.springframework.stereotype.Service


interface CarHardwareController {
    fun lockCar(car:Car)
    fun unlockCar(car:Car)
}

@Service
class CarHardwareControllerImpl(
    private val unlockCarCommand: UnlockCarCommand,
    private val lockCarCommand: LockCarCommand
):CarHardwareController{

    override fun lockCar(car: Car) = lockCarCommand.execute(car.id)

    override fun unlockCar(car: Car) = unlockCarCommand.execute(car.id)

}