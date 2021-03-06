package com.andrei.finalyearprojectapi.repositories

import com.andrei.finalyearprojectapi.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User,Long> {
    fun findTopById(id:Long):User?
    fun findTopByEmail(email:String):User?
    fun findTopByFirstNameAndLastName(firstName:String,lastName:String):User?
}