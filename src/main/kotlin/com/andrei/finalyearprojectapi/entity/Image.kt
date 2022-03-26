package com.andrei.finalyearprojectapi.entity

import javax.persistence.*

@Entity
class Image{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id")
    var id:Long = 0

    @Column(name = "url")
    var url:String = ""
    get() {
        return "https://robohash.org/$id"
    }


}