package com.andrei.finalyearprojectapi.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class IpAddress(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Long = 0
)
