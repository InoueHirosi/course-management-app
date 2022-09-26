package com.example.coursemanagementapp

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.coursemanagementapp.ui.home.HomeViewModel
import kotlinx.coroutines.*

class ViewHolderItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val ivHolder: ImageView = itemView.findViewById(R.id.iv)
    val tvNameHolder: TextView = itemView.findViewById(R.id.tvName)
    val tvTeacherNameHolder: TextView = itemView.findViewById(R.id.tvTeacherName)
    val tvTopicsHolder: TextView = itemView.findViewById(R.id.tvTopics)
    val tvNumberOfTopicsHolder: TextView = itemView.findViewById(R.id.tvNumberOfTopics)
    val dividerHolder: View = itemView.findViewById(R.id.divider)
    val tvIdHolder: TextView = itemView.findViewById(R.id.tvId)
    val tvNumberOfProgress: TextView = itemView.findViewById(R.id.tvNumberOfProgress)
    val bookBtn: ToggleButton = itemView.findViewById(R.id.tb)
    val progressBar: ProgressBar = itemView.findViewById(R.id.pbHorizontal)
    private var db: BookmarkDatabase
    private var dao: BookmarkDAO

    init {
        this.db = Room.databaseBuilder(
            itemView.context,
            BookmarkDatabase::class.java,"bookmark.db"
        ).build()
        this.dao = this.db.bookmarkDAO()

        bookBtn.setOnClickListener {
            if (bookBtn.isChecked) {// The toggle is enabled(on)
                Toast.makeText(
                    itemView.context,
                    itemView.context.resources.getString(R.string.text_on),
                    Toast.LENGTH_SHORT
                ).show()
                ivHolder.background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.border_yellow)
                writeOnCourseId(tvIdHolder.text.toString())
            } else {// The toggle is disabled(off)
                Toast.makeText(
                    itemView.context,
                    itemView.context.resources.getString(R.string.text_off),
                    Toast.LENGTH_SHORT
                ).show()
                ivHolder.background =
                    ContextCompat.getDrawable(itemView.context, R.drawable.border_gray)
                writeOffCourseId(tvIdHolder.text.toString())
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun writeOnCourseId(id: String){
        GlobalScope.launch{
            withContext(Dispatchers.IO) {
                val bookmark = Bookmark(id = id.hashCode(), txt = "true")
                dao.upsert(bookmark)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun writeOffCourseId(id: String){
        GlobalScope.launch{
            withContext(Dispatchers.IO) {
                val bookmark = Bookmark(id = id.hashCode(), txt = "false")
                dao.upsert(bookmark)
            }
        }
    }
}