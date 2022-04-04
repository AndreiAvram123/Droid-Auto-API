package com.andrei.finalyearprojectapi.entity

import javax.persistence.*

@Entity
class IpAddress(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Long,

    @Column(name = "value", nullable = false)
    val value:String
)
