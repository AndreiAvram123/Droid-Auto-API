package com.andrei.finalyearprojectapi.entity

import javax.persistence.*

@Entity
class Image(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id")
    val id:Long,
    @Column(name = "url")
    val url: String

)
