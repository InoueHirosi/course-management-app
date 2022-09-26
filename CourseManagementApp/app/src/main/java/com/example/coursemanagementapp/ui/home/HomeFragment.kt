package com.example.coursemanagementapp.ui.home

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
import com.example.coursemanagementapp.*
import com.example.coursemanagementapp.databinding.FragmentHomeBinding
import kotlinx.coroutines.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var recyclerView: RecyclerView
    private val binding get() = _binding!!
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var db: BookmarkDatabase
    private lateinit var dao: BookmarkDAO
    private var loading = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        //get courses and its progress by api
        homeViewModel.doWork()
        homeViewModel.progressBar.observe(viewLifecycleOwner) {

            if (!it && loading) {
                if (homeViewModel.courseInfo.value?.length()
                        ?.let { it1 ->
                            homeViewModel.courseInfo.value?.getJSONObject(it1 - 1)
                                ?.isNull("progress")
                        } == false
                ) {
                    //get bookmark status from Room database
                    lifecycleScope.launch {
                        getBookmark(homeViewModel)

                        if (!loading) {
                            //set data to adapter
                            recyclerView.adapter =
                                homeViewModel.courseInfo.value?.let { ci ->
                                    RecyclerAdapter(ci)
                                }

                            //delete loading bar
                            val progressBar: ProgressBar = binding.progressBar
                            progressBar.visibility = View.GONE

                        }
                    }
                } else if (homeViewModel.counter == homeViewModel.apiLimit) {
                    //Display connection error message
                    AlertDialog.Builder(requireContext())
                        .setTitle(requireContext().resources.getString(R.string.connection_error))
                        .setMessage(requireContext().resources.getString(R.string.retry_message))
                        .setPositiveButton("OK") { _, _ -> }.show()
                }
            } else {
                loading = true
            }
        }

        //refresh page by swiping(api and Room)
        swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout).apply {
            setOnRefreshListener {
                loading = true
                homeViewModel.doWork()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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
     * @param homeViewModel for course data with progress without bookmark data
     * @author Inoue Hiroshi "bookmarkFlag != null" should be written for Robustness
     */
    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun getBookmark(homeViewModel: HomeViewModel) {

        //Room setting
        this.db = Room.databaseBuilder(
            requireContext(), BookmarkDatabase::class.java, "bookmark.db"
        ).build()
        this.dao = this.db.bookmarkDAO()

        //false=>first login
        var loginFlag = false

        val result = homeViewModel.courseInfo.value ?: return
        for (i in 0 until result.length()) {
            val id = result.getJSONObject(i).getString("id").hashCode()
            val bookmarkFlag = withContext(Dispatchers.IO) {
                dao.getBookmark(id)
            }

            //if property is not in Room database, set false to Room database
            if (bookmarkFlag != null && bookmarkFlag.txt.isNotBlank()) {
                result.getJSONObject(i).put("bookmark", bookmarkFlag.txt)
                loginFlag = true
            } else {
                result.getJSONObject(i).put("bookmark", "false")
            }
        }
        //apply result to livedata
        homeViewModel.info.value = result

        //Display tips for first login user
        if (!loginFlag) {
            AlertDialog.Builder(requireContext())
                .setTitle(requireContext().resources.getString(R.string.tips))
                .setMessage(requireContext().resources.getString(R.string.tips_message))
                .setPositiveButton("OK") { _, _ ->

                    //set false flag to all courses' bookmark properties in Room database
                    for (i in 0 until result.length()) {
                        val bookmark = Bookmark(
                            id = result.getJSONObject(i).getString("id").hashCode(),
                            txt = "false"
                        )
                        GlobalScope.launch {
                            dao.upsert(bookmark)
                        }
                    }
                }.setCancelable(false).show()
        }
        loading = false
        //delete swipe progress bar
        swipeRefreshLayout.isRefreshing = false
    }
}