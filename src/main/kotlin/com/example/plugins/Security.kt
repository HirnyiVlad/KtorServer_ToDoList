package com.example.plugins

import com.example.session.Session
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<Session>("SESSION")
    }


    intercept(ApplicationCallPipeline.Plugins){
        if (call.sessions.get<Session>() == null){
            call.sessions.set(Session(generateNonce()))
        }
    }

}
