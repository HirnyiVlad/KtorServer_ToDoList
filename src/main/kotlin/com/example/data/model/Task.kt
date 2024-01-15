package com.example.data.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId


@Serializable
data class Task(
    val text: String,
    val description: String? =null,
    val isCompleted: Boolean = false,
    val timestamp: Long = 0,
    val color: String? = null,
    @BsonId
    val id:String = ObjectId().toString()
    )
