package com.andrei.finalyearprojectapi.repositories

import com.andrei.finalyearprojectapi.entity.FinishedRide
import org.springframework.data.repository.CrudRepository

sealed interface FinishedRideRepository: CrudRepository<FinishedRide,Long>{
    fun findTopById(id:Long):FinishedRide?
}