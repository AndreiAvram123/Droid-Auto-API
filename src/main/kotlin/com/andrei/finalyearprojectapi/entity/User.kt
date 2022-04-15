package com.andrei.finalyearprojectapi.entity

import com.andrei.finalyearprojectapi.entity.enums.UserRole
import com.andrei.finalyearprojectapi.entity.enums.UserRoleConverter
import com.fasterxml.jackson.annotation.JsonIgnore
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
    val lastName:String,
    @Column(nullable = false)
    val email:String,
    @Column(nullable = false)
    @JsonIgnore
    val password:String,
    @Convert(converter = UserRoleConverter::class)
    @JsonIgnore
    val role: UserRole = UserRole.USER,

    @OneToMany
    @JsonIgnore
    val ipAddresses:MutableList<IpAddress> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.PERSIST])
    @JsonIgnore
    val finishedRides:MutableList<FinishedRide> = mutableListOf(),

    @JsonIgnore
    val emailVerified:Boolean = false,

    @Column(
        nullable = true
    )
    @JsonIgnore
    var stripeCustomerID:String? = null,
)