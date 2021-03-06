package com.andrei.finalyearprojectapi.entity

import com.andrei.finalyearprojectapi.entity.enums.UserRole
import com.andrei.finalyearprojectapi.entity.enums.UserRoleConverter
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity(name = "users")
@Table(
    uniqueConstraints = [UniqueConstraint(name = "firstNameAndLastName", columnNames = arrayOf("first_name","last_name") )]
)
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userID")
    val id:Long,
    @Column(name = "first_name", nullable = false)
    val firstName:String,
    @Column(name = "last_name", nullable = false)
    val lastName:String,
    @Column(
        nullable = false,
        unique = true
    )
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

    @OneToMany
    @JsonIgnore
    val finishedRides:MutableList<FinishedRide> = mutableListOf(),

    @JsonIgnore
    var emailVerified:Boolean = false,

    @JsonIgnore
    var identityVerified:Boolean = false,

    @Column(
        nullable = true
    )
    @JsonIgnore
    var stripeCustomerID:String? = null,
)