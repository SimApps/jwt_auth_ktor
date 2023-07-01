package com.simapp.plugins

import com.simapp.authenticate
import com.simapp.data.user.UserDataSource
import com.simapp.getSecretInfo
import com.simapp.security.hashing.HashingService
import com.simapp.security.token.TokenConfig
import com.simapp.security.token.TokenService
import com.simapp.signIn
import com.simapp.signUp
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*


fun Application.configureRouting(
    userDataSource: UserDataSource,
    hashingService: HashingService,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        signIn(
            userDataSource = userDataSource,
            hashingService= hashingService,
            tokenService = tokenService,
            tokenConfig = tokenConfig
        )

        signUp(
            userDataSource = userDataSource,
            hashingService= hashingService
        )

        authenticate()

        getSecretInfo()

    }
}
