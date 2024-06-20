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
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.databinding.FragmentJobBinding
import com.trototrackapp.trototrack.ui.add.JobActivity
import com.trototrackapp.trototrack.ui.result.JobResultActivity
import com.trototrackapp.trototrack.ui.viewmodel.JobViewModel
import com.trototrackapp.trototrack.ui.viewmodel.ViewModelFactory
import org.json.JSONException
import org.json.JSONObject

class JobFragment : Fragment() {

    private var _binding: FragmentJobBinding? = null
    private val binding get() = _binding!!
    private val jobViewModel: JobViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }
    private var isRegistered: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
    }

    private fun setupObserver() {
        jobViewModel.getJob().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResultState.Loading -> {

                }
                is ResultState.Success -> {
                    val jobData = result.data.data
                    isRegistered = jobData != null

                    if (jobData == null) {
                        binding.applyButton.isEnabled = true
                        binding.checkButton.isEnabled = false
                        binding.applyButton.setOnClickListener {
                            val intent = Intent(activity, JobActivity::class.java)
                            startActivity(intent)
                        }
                    } else {
                        when (jobData.status) {
                            "Pending", "Approved" -> {
                                binding.applyButton.isEnabled = false
                                binding.checkButton.isEnabled = true
                            }
                            "Rejected" -> {
                                binding.applyButton.isEnabled = true
                                binding.checkButton.isEnabled = true
                                binding.applyButton.setOnClickListener {
                                    val intent = Intent(activity, JobActivity::class.java).apply {
                                        putExtra("id", jobData.id)
                                    }
                                    startActivity(intent)
                                }
                            }
                        }

                        binding.checkButton.setOnClickListener {
                            val intent = Intent(activity, JobResultActivity::class.java).apply {
                                putExtra("name", jobData.name)
                                putExtra("nik", jobData.nik)
                                putExtra("address", jobData.address)
                                putExtra("phone", jobData.phone)
                                putExtra("file", jobData.file)
                                putExtra("status", jobData.status)
                            }
                            startActivity(intent)
                        }
                    }
                }
                is ResultState.Error -> {
                    binding.applyButton.isEnabled = true
                    binding.checkButton.isEnabled = false
                    binding.applyButton.setOnClickListener {
                        val intent = Intent(activity, JobActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}