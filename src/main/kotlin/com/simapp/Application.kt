package com.simapp

import com.simapp.data.user.MongoUserDataSource
import com.simapp.data.user.User
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.simapp.plugins.*
import com.simapp.security.hashing.SHA256HashingService
import com.simapp.security.token.JwtTokenService
import com.simapp.security.token.TokenConfig
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

/*fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}*/

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)
fun Application.module() {
    val mongoPw = System.getenv("MONGO_PW")
    val dbName = "ktor-auth"
    val db = KMongo.createClient(
        connectionString = "mongodb+srv://amiramisimapp:$mongoPw@cluster0.mmagmwh.mongodb.net/$dbName?retryWrites=true&w=majority"
    ).coroutine
        .getDatabase(dbName)

    val userDataSource = MongoUserDataSource(db)
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer =  environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = 365L * 1000L * 60L * 60L * 24L,
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()
    configureSerialization()
    configureMonitoring()
    configureSecurity(config = tokenConfig)
    configureRouting(
        userDataSource = userDataSource,
        hashingService= hashingService,
        tokenService = tokenService,
        tokenConfig = tokenConfig
    )
}
