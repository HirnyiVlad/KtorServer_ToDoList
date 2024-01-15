package com.example.data.room

import io.ktor.websocket.*

data class Member(
    val sessionId: String,
    val socket: WebSocketSession
)
