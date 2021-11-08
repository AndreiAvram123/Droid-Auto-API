package com.andrei.finalyearprojectapi.entity

import javax.persistence.*

@Entity
class IpAddress(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id:Long = 0,

    @Column(name = "value", nullable = false)
    var value:String = ""
)
