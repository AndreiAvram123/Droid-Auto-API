
import com.andrei.finalyearprojectapi.entity.User
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT

import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

fun HttpServletRequest.getAccessToken():String?{
    return getHeader(JWTToken.headerName)
}





abstract class JWTToken {
    companion object {
        const val tokenPrefix = "Bearer "
        const val headerName = "Authorization"
        const val userIDKey = "userID"
    }
}


class DecodedJwt(token: String,
                 private val decryptionKey:String ): JWTToken() {
    private val decodedJWT: DecodedJWT? = decodeToken(token)
    val userID:Long? = decodedJWT?.getClaim(userIDKey)?.asLong()

    fun isValid() = decodedJWT?.subject != null && userID != null


    private fun decodeToken(token:String): DecodedJWT?{
        val verifier = JWT.require(Algorithm.HMAC512(decryptionKey.toByteArray()))
            .build()
        try {
            return verifier.verify(token.replace(tokenPrefix,""))
        }   catch (e:Exception){
            return null
        }

    }
}

@OptIn(ExperimentalTime::class)
class EncryptedJWTToken constructor(user:User,
                                    private val encryptionKey:String,
                                    private val duration: Duration) : JWTToken() {

    val rawValue = generateTokenForUser(user)
    private fun generateTokenForUser(user: User): String {
        val expirationDate = Date(System.currentTimeMillis() + duration.inWholeMilliseconds)
        return JWT.create().withSubject(user.username)
            .withClaim(userIDKey, user.id)
            .withExpiresAt(expirationDate).sign(Algorithm.HMAC512(encryptionKey.toByteArray()))
    }
}