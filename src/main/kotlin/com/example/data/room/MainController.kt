package com.example.data.room

import com.example.data.TaskDataSource
import com.example.data.model.Task
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class MainController(
    private val taskDataSource: TaskDataSource
) {
    private lateinit var member: Member
    fun onJoin(
        sessionId: String,
        socket: WebSocketSession
    ) {
        member = Member(
            sessionId = sessionId,
            socket = socket
        )
    }

    suspend fun createTask(
        text: String,
        description: String?,
        color: String?,
    ) {
        val taskEntity = Task(
            text = text,
            description = description ?: "",
            isCompleted = false,
            color = color ?: "White",
            timestamp = System.currentTimeMillis()
        )
        taskDataSource.insertAllTasks(taskEntity)
        val parsedMessage = Json.encodeToString(taskEntity)
        member.socket.send(Frame.Text(parsedMessage))
    }

    suspend fun deleteTask(id :String){
        taskDataSource.deleteTaskById(id)
    }
    suspend fun updateTaskById(id:String, request: Task): Boolean{
      return taskDataSource.updateTaskById(id, request)
    }
    suspend fun updateTaskCompleteness(id: String, request: Task):Boolean{
        return taskDataSource.updateTaskCompleteness(id, request)
    }
    suspend fun getAllMessages(): List<Task> {
        return taskDataSource.getAllTasks()
    }
    suspend fun tryDisconnect(){
        member.socket.close()
    }
}