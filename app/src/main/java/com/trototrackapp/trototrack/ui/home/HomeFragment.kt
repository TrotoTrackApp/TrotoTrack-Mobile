package com.trototrackapp.trototrack.ui.home

import android.content.Intent
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
import com.trototrackapp.trototrack.databinding.FragmentHomeBinding
import com.trototrackapp.trototrack.ui.adapter.GetArticleAdapter
import com.trototrackapp.trototrack.ui.detail.DetailAccountActivity
import com.trototrackapp.trototrack.ui.viewmodel.GetArticleViewModel
import com.trototrackapp.trototrack.ui.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var getArticleAdapter: GetArticleAdapter
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val getArticleViewModel: GetArticleViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObserver()

        binding.detailAccountButton.setOnClickListener {
            val intent = Intent(activity, DetailAccountActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        getArticleAdapter = GetArticleAdapter()
        binding.recycleViewArticle.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = getArticleAdapter
        }
    }

    private fun setupObserver() {
        getArticleViewModel.getArticle().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }
                is ResultState.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    getArticleAdapter.submitList(result.data.data)
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
