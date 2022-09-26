package com.example.coursemanagementapp

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BookmarkDAO {

    @Query("select * from bookmark where id = :id")
    fun getBookmark(id: Int): Bookmark

    @Query("select * from bookmark")
    fun getAll(): LiveData<List<Bookmark>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(bookmark: Bookmark): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(bookmark: Bookmark)

    @Transaction
    fun upsert(bookmark: Bookmark) {
        val id = insert(bookmark)
        if (id == -1L) {
            update(bookmark)
        }
    }

    @Query("delete from bookmark")
    fun deleteAll()
}