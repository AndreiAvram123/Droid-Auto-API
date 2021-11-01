package com.andrei.finalyearprojectapi.repositories

import com.andrei.finalyearprojectapi.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User,Long> {
    fun findTopByUsername(username:String):User?
    fun findTopById(id:Long):User?
}