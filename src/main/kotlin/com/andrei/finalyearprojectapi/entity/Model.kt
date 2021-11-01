package com.andrei.finalyearprojectapi.entity

import javax.persistence.*

@Entity
class Model(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "modelID")
    var id: Long = 0,


    @Column(name = "model_name")
    var name:String = ""
)
