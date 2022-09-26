package com.example.coursemanagementapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bookmark(
    @PrimaryKey(autoGenerate = true)
    var id: Int, val txt: String) {

}