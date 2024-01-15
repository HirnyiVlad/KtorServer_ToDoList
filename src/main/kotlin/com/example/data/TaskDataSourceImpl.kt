package com.example.data

import com.example.data.model.Task
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.replaceOne

class TaskDataSourceImpl(
    private val db: CoroutineDatabase
) : TaskDataSource {
    private val tasks = db.getCollection<Task>()

    override suspend fun getAllTasks(): List<Task> {
        return tasks.find()
            // .descendingSort(Task::color)
            .toList()
    }

    override suspend fun insertAllTasks(task: Task) {
        tasks.insertOne(task)
    }

    override suspend fun deleteTaskById(id: String) {
        tasks.deleteOneById(id)
    }

    override suspend fun updateTaskById(id: String, request: Task): Boolean =
        tasks.findOneById(id)?.let { task ->
            val updateResult = tasks.replaceOne(
                task.copy(
                    text = request.text,
                    description = request.description,
                    isCompleted = task.isCompleted,
                    timestamp = task.timestamp,
                    color = request.color,
                    id = task.id
                )
            )
            updateResult.modifiedCount == 1L
        } ?: false

    override suspend fun updateTaskCompleteness(id: String, request: Task): Boolean =
        tasks.findOneById(id)?.let { task ->
            val updatedIsComplete = !request.isCompleted
            val updateResult = tasks.replaceOne(
                task.copy(
                    text = task.text,
                    description = task.description,
                    isCompleted = updatedIsComplete,
                    timestamp = task.timestamp,
                    color = task.color,
                    id = task.id
                )
            )
            updateResult.modifiedCount == 1L
        } ?: false

}