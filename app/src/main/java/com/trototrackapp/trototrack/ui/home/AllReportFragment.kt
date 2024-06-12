package com.trototrackapp.trototrack.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.databinding.FragmentAllReportBinding
import com.trototrackapp.trototrack.ui.adapter.AllReportsAdapter
import com.trototrackapp.trototrack.ui.viewmodel.ReportsViewModel
import com.trototrackapp.trototrack.ui.viewmodel.ViewModelFactory
import org.json.JSONException
import org.json.JSONObject

class AllReportFragment : Fragment() {

    private lateinit var allReportsAdapter: AllReportsAdapter
    private var _binding: FragmentAllReportBinding? = null
    private val binding get() = _binding!!
    private val reportsViewModel: ReportsViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllReportBinding.inflate(inflater, container, false)
        val view = binding.root

        setupRecyclerView()
        setupObserver()

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                allReportsAdapter.filterData(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No action needed here
            }
        })

        return view
    }

    private fun setupRecyclerView() {
        allReportsAdapter = AllReportsAdapter()
        binding.recycleViewReports.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = allReportsAdapter
        }
    }

    private fun setupObserver() {
        reportsViewModel.getAllReports().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }
                is ResultState.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    allReportsAdapter.submitList(result.data.data)
                }
                is ResultState.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                    val errorMessage = result.message.let {
                        try {
                            val json = JSONObject(it)
                            json.getString("message")
                        } catch (e: JSONException) {
                            it
                        }
                    } ?: "An error occurred"
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
