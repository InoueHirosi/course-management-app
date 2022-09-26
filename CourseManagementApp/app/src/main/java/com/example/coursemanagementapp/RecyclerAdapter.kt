package com.example.coursemanagementapp

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.json.JSONArray

class RecyclerAdapter(list: JSONArray) :
    RecyclerView.Adapter<ViewHolderItem>() {
    private val courseList = list

    //generate one layer
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderItem {
        val itemXml = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_layout, parent, false)
        return ViewHolderItem(itemXml)
    }

    //Set data[position] in layout
    override fun onBindViewHolder(holder: ViewHolderItem, position: Int) {
        holder.tvNameHolder.text = courseList.getJSONObject(position).getString("name")
        holder.tvTeacherNameHolder.text =
            courseList.getJSONObject(position).getString("teacher_name")
        holder.tvNumberOfTopicsHolder.text =
            courseList.getJSONObject(position).getString("number_of_topics")
        holder.tvIdHolder.text = courseList.getJSONObject(position).getString("id")
        holder.tvNumberOfProgress.text = buildString {
            append(courseList.getJSONObject(position).getString("progress"))
            append(" %")
        }
        holder.progressBar.max = 100
        holder.progressBar.progress = 0
        holder.progressBar.min = 0

        if (courseList.getJSONObject(position).getString("icon_url") != "") {
            Picasso.get()
                .load(courseList.getJSONObject(position).getString("icon_url"))
                .placeholder(R.drawable.ic_launcher_foreground)
                .resize(160, 160)
                .centerCrop()
                .into(holder.ivHolder)
            if (!courseList.getJSONObject(position).isNull("bookmark")
                && courseList.getJSONObject(position).getString("bookmark") == "true"
            ) {
                holder.ivHolder.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.border_yellow)
                holder.bookBtn.isChecked = true
            } else {
                holder.ivHolder.background =
                    ContextCompat.getDrawable(holder.itemView.context, R.drawable.border_gray)
                holder.bookBtn.isChecked = false
            }
        } else {
            holder.dividerHolder.visibility = View.GONE
            holder.ivHolder.visibility = View.GONE
            holder.tvTopicsHolder.visibility = View.GONE
            holder.progressBar.visibility = View.GONE
        }

        setProgressBar(
            holder.progressBar,
            Integer.parseInt(courseList.getJSONObject(position).getString("progress"))
        )
    }

    //Count the number of data
    override fun getItemCount(): Int {
        return courseList.length()
    }

    private fun setProgressBar(bar: ProgressBar, progressValue: Int) {
        val animation = ObjectAnimator.ofInt(bar, "progress", progressValue)
        animation.duration = 500
        animation.interpolator = DecelerateInterpolator()
        animation.start()
    }
}