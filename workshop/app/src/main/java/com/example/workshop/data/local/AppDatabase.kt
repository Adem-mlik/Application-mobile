package com.example.workshop.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.workshop.data.Model.Task

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}
