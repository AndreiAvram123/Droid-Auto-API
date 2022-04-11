package com.andrei.finalyearprojectapi.repositories

import com.andrei.finalyearprojectapi.entity.Ride
import org.springframework.data.repository.CrudRepository

sealed interface FinishedRideRepository: CrudRepository<Ride,Long>{
    fun findTopById(id:Long):Ride?
}