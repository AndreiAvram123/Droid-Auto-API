package com.andrei.finalyearprojectapi.entity

import com.andrei.finalyearprojectapi.entity.enums.UserRole
import com.andrei.finalyearprojectapi.entity.enums.UserRoleConverter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userID")
    val id:Long,
    @Column(name = "firstName", nullable = false)
    val firstName:String,
    @Column(name = "lastName", nullable = false)
    val lastName:String ,
    @Column(nullable = false)
    val email:String ,
    @Column(nullable = false)
    val emailVerified:Boolean = false,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonIgnore
    val password:String ,
    @Convert(converter = UserRoleConverter::class)
    val role:UserRole = UserRole.USER,

    @OneToMany
    @JsonIgnore
    val ipAddresses:MutableList<IpAddress> = mutableListOf()
)