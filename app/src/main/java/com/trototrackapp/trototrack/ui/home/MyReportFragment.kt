package com.trototrackapp.trototrack.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.databinding.FragmentMyReportBinding
import com.trototrackapp.trototrack.ui.adapter.GetReportsUserAdapter
import com.trototrackapp.trototrack.ui.viewmodel.GetReportsViewModel
import com.trototrackapp.trototrack.ui.viewmodel.ViewModelFactory

class MyReportFragment : Fragment() {

    private lateinit var reportsUserAdapter: GetReportsUserAdapter
    private var _binding: FragmentMyReportBinding? = null
    private val binding get() = _binding!!
    private val getMyReportsViewModel: GetReportsViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyReportBinding.inflate(inflater, container, false)
        val view = binding.root

        setupRecyclerView()
        setupObserver()

        return view
    }

    private fun setupRecyclerView() {
        reportsUserAdapter = GetReportsUserAdapter()
        binding.recycleViewReports.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reportsUserAdapter
        }
    }

    private fun setupObserver() {
        getMyReportsViewModel.getReportsUser().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }
                is ResultState.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    reportsUserAdapter.submitList(result.data.data)
                }
                is ResultState.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                    Toast.makeText(context, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}