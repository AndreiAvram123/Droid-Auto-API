package com.andrei.finalyearprojectapi.filters

import DecodedJwt
import com.andrei.finalyearprojectapi.entity.User

open class UserDataObject{
    var user:User? = null
    var decodedToken:DecodedJwt? = null
}


