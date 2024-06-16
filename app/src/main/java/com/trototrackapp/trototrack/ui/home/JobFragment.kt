package com.trototrackapp.trototrack.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        binding.applyButton.setOnClickListener {
            if (isRegistered) {
                AlertDialog.Builder(requireContext())
                    .setMessage("You have registered")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            } else {
                val intent = Intent(activity, JobActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setupObserver() {
        jobViewModel.getJob().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResultState.Loading -> {

                }
                is ResultState.Success -> {
                    binding.checkButton.setOnClickListener {
                        val intent = Intent(activity, JobResultActivity::class.java).apply {
                            putExtra("name", result.data.data?.name)
                            putExtra("nik", result.data.data?.nik)
                            putExtra("address", result.data.data?.address)
                            putExtra("phone", result.data.data?.phone)
                            putExtra("file", result.data.data?.file)
                        }
                        startActivity(intent)
                    }
                    isRegistered = result.data.data != null
                    binding.applyButton.isEnabled = !isRegistered
                }
                is ResultState.Error -> {
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
