package com.andrei.finalyearprojectapi

import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.utils.JWTTokenUtility
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JwtTests {
    @Autowired
    private lateinit var jwtTokenUtility: JWTTokenUtility
    private val encryptionKeyAccessToken = "TestEncryptionKey"

    @Test
    fun `encrypted token can be  decrypted and contain the userID `(){
        val user = User(
            id = 2,
            username = "andrei",
            password = "andrei123"
        )
        val encodedToken = jwtTokenUtility.generateAccessToken(user).rawValue
        val decodedToken = jwtTokenUtility.decodeAccessToken(encodedToken)
        assert(user.id == decodedToken.userID)
    }




}