package com.andrei.finalyearprojectapi.entity.redis

enum class ReservationKeys(val value:String) {
    USER_ID("user.id"), CAR_ID("car.id")
}


enum class CarKeys(val value:String){
    STATUS("status")
}
enum class RideKeys(val value :String){
    TIME_STARTED("time.started"),CAR_ID("car.id"), USER_ID("user.id")
}

enum class CarStatus(val value :String){
    RESERVED("reserved"), IN_USE("in_use"),
}