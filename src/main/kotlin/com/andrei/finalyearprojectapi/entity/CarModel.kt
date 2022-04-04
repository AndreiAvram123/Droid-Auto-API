package com.andrei.finalyearprojectapi.entity

import javax.persistence.*

@Entity(name = "model")
class CarModel(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "modelID")
    val id: Long,

    @Column(
        name = "model_name",
        nullable = false
    )
    val name:String,

    @Column(
        name = "manufacturer_name",
        nullable = false
    )
    val manufacturerName:String,

    @OneToOne(
        optional = false,
        orphanRemoval = true,
        cascade = [
            CascadeType.REMOVE,
            CascadeType.PERSIST]
    )
    val image:Image,

)
