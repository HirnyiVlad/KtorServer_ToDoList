package com.example.data

import com.example.data.model.Task

interface TaskDataSource {
    suspend fun getAllTasks(): List<Task>
    suspend fun insertAllTasks(task: Task)
    suspend fun deleteTaskById(id: String)
    suspend fun updateTaskById(id: String, request: Task): Boolean
    suspend fun updateTaskCompleteness(id:String, request: Task):Boolean
}