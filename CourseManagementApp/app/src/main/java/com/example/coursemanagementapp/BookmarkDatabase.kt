package com.example.coursemanagementapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Bookmark::class], version = 1, exportSchema = false)
abstract class BookmarkDatabase: RoomDatabase() {
    abstract fun bookmarkDAO(): BookmarkDAO
}
