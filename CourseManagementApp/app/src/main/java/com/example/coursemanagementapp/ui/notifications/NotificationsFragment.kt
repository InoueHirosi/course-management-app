package com.example.coursemanagementapp.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.coursemanagementapp.R
import com.example.coursemanagementapp.RecyclerAdapter
import com.example.coursemanagementapp.databinding.FragmentNotificationsBinding
import com.example.coursemanagementapp.ui.home.HomeViewModel
import it.mirko.rangeseekbar.RangeSeekBar
import kotlinx.coroutines.launch
import org.json.JSONArray


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var loading = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        val notificationsViewModel =
            ViewModelProvider(requireActivity())[NotificationsViewModel::class.java]

        //RangeSeekBar and its text settings
        val rangeSeekBar: RangeSeekBar = binding.rangeSeekBar
        val startText: TextView = binding.startText
        val endText: TextView = binding.endText

        if (notificationsViewModel.startText.value == null) {
            notificationsViewModel.startValue.value = 0
        }
        if (notificationsViewModel.endText.value == null) {
            notificationsViewModel.endValue.value = 100
        }
        startText.text = buildString {
            append(notificationsViewModel.startText.value.toString())
            append(" %")
        }
        endText.text = buildString {
            append(notificationsViewModel.endText.value.toString())
            append(" %")
        }
        rangeSeekBar.startProgress =
            Integer.parseInt(notificationsViewModel.startValue.value.toString())
        rangeSeekBar.endProgress = Integer.parseInt(notificationsViewModel.endText.value.toString())
        rangeSeekBar.setMinDifference(10)

        //listener of RangeSeekBar. If start or end value is changed, apply it to TextView
        rangeSeekBar.setOnRangeSeekBarListener { _, start, end ->
            startText.text = buildString {
                append(start.toString())
                append(" %")
            }
            endText.text = buildString {
                append(end.toString())
                append(" %")
            }
            notificationsViewModel.startValue.value = start
            notificationsViewModel.endValue.value = end
        }

        //Observer. If start or end value is changed, apply it to course data list
        notificationsViewModel.startText.observe(viewLifecycleOwner) {
            applyStartEnd(homeViewModel, notificationsViewModel)
        }
        notificationsViewModel.endText.observe(viewLifecycleOwner) {
            applyStartEnd(homeViewModel, notificationsViewModel)
        }

        if (homeViewModel.courseInfo.value?.length()
                ?.let {
                    homeViewModel.courseInfo.value?.getJSONObject(it - 1)?.isNull("bookmark")
                } == false
        ) {
            //get bookmark status from Room database
            applyStartEnd(homeViewModel, notificationsViewModel)

            if (!loading) {
                //delete loading bar
                val progressBar: ProgressBar = binding.progressBar
                progressBar.visibility = View.GONE
                //set data to adapter
                recyclerView.adapter =
                    notificationsViewModel.courseInfo.value?.let { ci ->
                        RecyclerAdapter(ci)
                    }
            }
        }

        //Observer which arrange data automatically
        notificationsViewModel.courseInfo.observe(viewLifecycleOwner) {

            if (!loading) {
                //delete loading bar
                val progressBar: ProgressBar = binding.progressBar
                progressBar.visibility = View.GONE
                //set data to adapter
                recyclerView.adapter =
                    notificationsViewModel.courseInfo.value?.let { ci ->
                        RecyclerAdapter(ci)
                    }
            }
        }

        //refresh page by swiping(RangeSeekBar)
        swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout).apply {
            setOnRefreshListener {
                lifecycleScope.launch {
                    applyStartEnd(homeViewModel, notificationsViewModel)
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
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
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
     * Judge data(progress) range
     * @param homeViewModel used for sharing course data from HomeViewModel livedata
     * @param notificationsViewModel used for refer range values to show
     */
    private fun applyStartEnd(
        homeViewModel: HomeViewModel,
        notificationsViewModel: NotificationsViewModel
    ) {
        val appliedJSONArray = JSONArray()
        val result = homeViewModel.courseInfo.value ?: return
        for (i in 0 until result.length()) {
            val underLimit: Boolean = notificationsViewModel.startText.value!! <= Integer.parseInt(
                result.getJSONObject(i).getString("progress")
            )
            val upperLimit: Boolean = notificationsViewModel.endText.value!! >= Integer.parseInt(
                result.getJSONObject(i).getString("progress")
            )
            if (underLimit && upperLimit) {
                appliedJSONArray.put(result.getJSONObject(i))
            }
        }
        sortByProgress(appliedJSONArray, notificationsViewModel)
    }

    /**
     * sort course data by progress % (ascending order)
     * @param appliedJSONArray used for data box
     * @param notificationsViewModel used for setting course data to show list
     */
    private fun sortByProgress(
        appliedJSONArray: JSONArray,
        notificationsViewModel: NotificationsViewModel
    ) {
        val sortedJsonArray = JSONArray()
        val mMap = mutableMapOf<Int, Int>()
        for (i in 0 until appliedJSONArray.length()) {
            mMap[i] = Integer.parseInt(appliedJSONArray.getJSONObject(i).getString("progress"))
        }
        val sortedMap = mMap.toList().sortedBy { it.second }.toMap()
        sortedMap.forEach { (t, _) ->
            sortedJsonArray.put(appliedJSONArray.getJSONObject(t))
        }
        notificationsViewModel.info.value = sortedJsonArray
        loading = false
    }
}