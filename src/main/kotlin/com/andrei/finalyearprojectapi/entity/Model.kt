package com.andrei.finalyearprojectapi.entity

import javax.persistence.*

@Entity
class Model(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "modelID")
    var id: Long = 0,

    @Column(
        name = "model_name",
        nullable = false
    )
    var name:String = "",

    @Column(
        name = "manufacturer_name",
        nullable = false
    )
    var manufacturerName:String = "",

    @OneToOne(
        optional = true,
        orphanRemoval = true,
        cascade = [
            CascadeType.REMOVE,
            CascadeType.PERSIST]
    )
    var image:Image = Image()
)
