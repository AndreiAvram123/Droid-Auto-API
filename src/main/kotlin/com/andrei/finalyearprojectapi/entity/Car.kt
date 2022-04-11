package com.andrei.finalyearprojectapi.entity

import javax.persistence.*

@Entity
class Car(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "carID")
    val id: Long,

    @ManyToOne
    val model:CarModel,

    @Embedded
    val location:LatLng,

    @Column(
        name = "price_per_minute",
        nullable = false
    )
    val pricePerMinute:Long

)