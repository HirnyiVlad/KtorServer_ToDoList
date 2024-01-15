package com.example.route

import com.example.data.model.Task
import com.example.data.room.MainController
import com.example.main
import com.example.session.Session
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.litote.kmongo.json

fun Route.roomSocketRoute(mainController: MainController) {
    webSocket("/main-socket") {
        val session = call.sessions.get<Session>()
        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session."))
            return@webSocket
        }
        try {
            mainController.onJoin(
                sessionId = session.sessionId,
                socket = this
            )
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val jsonString = frame.readText()
                    try {
                        val task = Json.decodeFromString<Task>(jsonString)
                        mainController.createTask(
                            text = task.text,
                            description = task.description,
                            color = task.color
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            mainController.tryDisconnect()
        }
    }
}

fun Route.getAllTasks(mainController: MainController) {
    get("/tasks") {
        call.respond(
            HttpStatusCode.OK,
            mainController.getAllMessages()
        )
    }
}

fun Route.deleteTaskById(mainController: MainController){
    delete("/{id}") {
        val id = call.parameters["id"].toString()
        call.respond(
            HttpStatusCode.OK,
            mainController.deleteTask(id)
        )
    }
}

fun Route.updateTaskById(mainController: MainController){
    put("/{id}") {
        val id = call.parameters["id"].toString()
        val isTaskEdit: Boolean = call.parameters["task_edit"].toBoolean()

        val taskRequest = call.receive<Task>()

        val updatedSuccessfully =if (isTaskEdit){
            mainController.updateTaskById(id, taskRequest)
        }else{
            mainController.updateTaskCompleteness(id, taskRequest)
        }
        if(updatedSuccessfully){
            call.respond(HttpStatusCode.OK)
        }else{
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}
