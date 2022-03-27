package com.andrei.finalyearprojectapi.entity

import javax.persistence.*

@Entity
class Car(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "carID")
    var id: Long = 0,

    @ManyToOne
    var model:Model = Model(),

    @Embedded
    val location:LatLng = LatLng(),


)