package com.andrei.finalyearprojectapi.repositories

import com.andrei.finalyearprojectapi.entity.Car
import org.springframework.data.jpa.repository.JpaRepository

interface CarRepository : JpaRepository<Car,Long>{
}