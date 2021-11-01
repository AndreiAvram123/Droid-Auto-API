package com.andrei.finalyearprojectapi.entity

import com.andrei.finalyearprojectapi.entity.enums.UserRole
import com.andrei.finalyearprojectapi.entity.enums.UserRoleConverter
import com.fasterxml.jackson.annotation.JsonProperty
import com.squareup.moshi.JsonClass
import javax.persistence.*

@Entity(name = "users")
@JsonClass(generateAdapter = true)
class User (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userID")
    var id:Long = 0,
    @Column(name = "username")
    var username:String = "",
    var email:String = "",
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    var password:String = "",
    @Convert(converter = UserRoleConverter::class)
    var role:UserRole = UserRole.USER,
)