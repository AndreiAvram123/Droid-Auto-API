package com.andrei.finalyearprojectapi

import com.andrei.finalyearprojectapi.configuration.TestDetails
import com.andrei.finalyearprojectapi.entity.User
import com.andrei.finalyearprojectapi.utils.JWTFactory
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("local")
class JwtTests {

    @Autowired
    private lateinit var jwtFactory: JWTFactory

    @Test
    fun `encrypted token can be  decrypted and contain the userID `(){
        val user = User(
             id = TestDetails.testId,
            password = TestDetails.testPassword
        )
        val encodedToken = jwtFactory.generateAccessToken(user).value
        val decodedToken = jwtFactory.decodeAccessToken(encodedToken)
        assert(user.id == decodedToken.userID)
    }




}