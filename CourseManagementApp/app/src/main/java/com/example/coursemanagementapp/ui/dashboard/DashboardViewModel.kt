package com.example.coursemanagementapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONArray

class DashboardViewModel : ViewModel() {

    val info = MutableLiveData<JSONArray>()
    var courseInfo: LiveData<JSONArray> = info

}