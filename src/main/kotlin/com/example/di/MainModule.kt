package com.example.di

import com.example.data.TaskDataSourceImpl
import com.example.data.TaskDataSource
import com.example.data.room.MainController
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("tasks_db")
    }
    single<TaskDataSource> {
        TaskDataSourceImpl(get())
    }
    single {
        MainController(get())
    }
}