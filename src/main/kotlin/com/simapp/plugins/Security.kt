package com.simapp.plugins

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.simapp.security.token.TokenConfig
import io.ktor.server.application.*

fun Application.configureSecurity(config : TokenConfig) {
    
    // Please read the jwt property from the config file if you are using EngineMain
    val jwtAudience = "users"
    val jwtDomain = "http://0.0.0.0:8080"
    val jwtRealm = "simapp ktor jwt auth app"
    val jwtSecret = "secret"
    val jwtIssuer = "http://0.0.0.0:8080"
    authentication {
        jwt {
           // realm = jwtRealm
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC256(config.secret))
                    .withAudience(config.audience)
                    .withIssuer(config.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(config.audience))
                    JWTPrincipal(credential.payload)
                else null
            }
        }
    }
}
