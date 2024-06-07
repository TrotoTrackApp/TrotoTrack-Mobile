package com.trototrackapp.trototrack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.trototrackapp.trototrack.R
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.databinding.FragmentAllReportBinding
import com.trototrackapp.trototrack.ui.adapter.GetAllReportsAdapter
import com.trototrackapp.trototrack.ui.viewmodel.GetReportsViewModel
import com.trototrackapp.trototrack.ui.viewmodel.ViewModelFactory

class AllReportFragment : Fragment() {

    private lateinit var allReportsAdapter: GetAllReportsAdapter
    private var _binding: FragmentAllReportBinding? = null
    private val binding get() = _binding!!
    private val getAllReportsViewModel: GetReportsViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllReportBinding.inflate(inflater, container, false)
        val view = binding.root

        setupRecyclerView()
        setupObserver()

        return view
    }

    private fun setupRecyclerView() {
        allReportsAdapter = GetAllReportsAdapter()
        binding.recycleViewReports.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = allReportsAdapter
        }
    }

    private fun setupObserver() {
        getAllReportsViewModel.getAllReports().observe(viewLifecycleOwner, Observer { result ->
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
                    Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
