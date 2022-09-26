package com.example.coursemanagementapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.coursemanagementapp.BookmarkDAO
import com.example.coursemanagementapp.BookmarkDatabase
import com.example.coursemanagementapp.R
import com.example.coursemanagementapp.RecyclerAdapter
import com.example.coursemanagementapp.databinding.FragmentDashboardBinding
import com.example.coursemanagementapp.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var db: BookmarkDatabase
    private lateinit var dao: BookmarkDAO
    private var loading = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        val dashboardViewModel = ViewModelProvider(requireActivity())[DashboardViewModel::class.java]

        if (homeViewModel.courseInfo.value?.length()
                ?.let {
                    homeViewModel.courseInfo.value?.getJSONObject(it - 1)?.isNull("progress")
                } == false
        ) {

            //get bookmark status from Room database
            lifecycleScope.launch {
                do {
                    getBookmark(homeViewModel, dashboardViewModel)
                } while (loading)

                //delete loading bar
                val progressBar: ProgressBar = binding.progressBar
                progressBar.visibility = View.GONE
                //set data to adapter
                recyclerView.adapter =
                    dashboardViewModel.courseInfo.value?.let { ci ->
                        RecyclerAdapter(ci)
                    }
            }
        } else {
            //Display connection error message
            AlertDialog.Builder(requireContext())
                .setTitle(requireContext().resources.getString(R.string.connection_error))
                .setMessage(requireContext().resources.getString(R.string.return_message))
                .setPositiveButton("OK") { _, _ -> }.show()
        }

        //refresh page by swiping(Room)
        swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout).apply {
            setOnRefreshListener {
                lifecycleScope.launch {
                    getBookmark(homeViewModel, dashboardViewModel)
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        recyclerView = root.findViewById(R.id.rv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * get and set(if data don't exist) bookmark data from Room database
     * @param homeViewModel for course data with progress and bookmark data + doWork method
     * @param dashboardViewModel used for setting course data to show list
     * @author Inoue Hiroshi "bookmarkFlag != null" should be written for Robustness
     */
    private suspend fun getBookmark(
        homeViewModel: HomeViewModel,
        dashboardViewModel: DashboardViewModel
    ) {
        this.db = Room.databaseBuilder(
            requireContext(), BookmarkDatabase::class.java, "bookmark.db"
        ).build()
        this.dao = this.db.bookmarkDAO()

        val bookmarkResult = JSONArray()

        val result = homeViewModel.courseInfo.value
        if (result == null) {
            //with no course data, get data by api
            homeViewModel.doWork()
            return
        }
        for (i in 0 until result.length()) {
            val id = result.getJSONObject(i).getString("id").hashCode()
            val bookmarkFlag = withContext(Dispatchers.IO) {
                dao.getBookmark(id)
            }
            if (bookmarkFlag != null && bookmarkFlag.txt == "true") {
                result.getJSONObject(i).put("bookmark", bookmarkFlag.txt)
                bookmarkResult.put(result.getJSONObject(i))
            }
        }
        dashboardViewModel.info.value = bookmarkResult
        loading = false
    }

}