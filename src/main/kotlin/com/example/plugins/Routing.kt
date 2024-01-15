package com.example.plugins

import com.example.data.room.MainController
import com.example.main
import com.example.route.deleteTaskById
import com.example.route.getAllTasks
import com.example.route.roomSocketRoute
import com.example.route.updateTaskById
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val mainController by inject<MainController>()
    install(Routing){
        roomSocketRoute(mainController)
        getAllTasks(mainController)
        deleteTaskById(mainController)
        updateTaskById(mainController)
    }
}
