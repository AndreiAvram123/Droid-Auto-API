package com.andrei.finalyearprojectapi.entity

import javax.persistence.*
@Entity
class FinishedRide(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Long,

    //time in unix
     @Column(
         nullable = false,
     )
    val startTime:Long,

    @Column(
        nullable = false
    )
    val endTime:Long,

    @Column(
        nullable = false,
    )
    val totalCharge:Long,

    @ManyToOne
    val user:User,

    @ManyToOne
    val car:Car
)