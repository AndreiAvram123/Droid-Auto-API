package com.andrei.finalyearprojectapi.entity

import com.andrei.finalyearprojectapi.entity.enums.UserRole
import com.andrei.finalyearprojectapi.entity.enums.UserRoleConverter
import com.fasterxml.jackson.annotation.JsonProperty

import javax.persistence.*

@Entity(name = "users")
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userID")
    var id:Long = 0,
    @Column(name = "firstName", nullable = false)
    var firstName:String = "",
    @Column(name = "lastName", nullable = false)
    var lastName:String = "",
    @Column(nullable = false)
    var email:String = "",
    @Column(nullable = false)
    var emailVerified:Boolean = false,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var password:String = "",
    @Convert(converter = UserRoleConverter::class)
    var role:UserRole = UserRole.USER,
    @OneToMany
    var ipAddresses:MutableList<IpAddress> = mutableListOf()
)