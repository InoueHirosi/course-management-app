package com.example.coursemanagementapp.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONArray

class NotificationsViewModel : ViewModel() {

    val info = MutableLiveData<JSONArray>()
    var courseInfo: LiveData<JSONArray> = info

    val startValue = MutableLiveData<Int>()
    val startText: LiveData<Int> = startValue

    val endValue = MutableLiveData<Int>()
    val endText: LiveData<Int> = endValue

}