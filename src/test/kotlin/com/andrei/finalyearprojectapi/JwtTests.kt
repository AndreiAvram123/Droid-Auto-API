package com.andrei.finalyearprojectapi

import com.andrei.finalyearprojectapi.configuration.TestDetails
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.utils.JWTUtils
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("local")
class JwtTests {

    @Autowired
    private lateinit var jwtUtils: JWTUtils

    @Test
    fun `encrypted token can be  decrypted and contain the userID `(){
        val user = User(
             id = TestDetails.testId,
            password = TestDetails.testPassword
        )
        val encodedToken = jwtUtils.generateAccessToken(user).value
        val decodedToken = jwtUtils.decodeAccessToken(encodedToken)
        assert(user.id == decodedToken.userID)
    }




}