package com.trototrackapp.trototrack.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.databinding.FragmentToolsBinding
import com.trototrackapp.trototrack.ui.result.ResultActivity
import com.trototrackapp.trototrack.ui.viewmodel.ScanViewModel
import com.trototrackapp.trototrack.ui.viewmodel.ViewModelFactory
import com.trototrackapp.trototrack.util.getImageUri
import com.trototrackapp.trototrack.util.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class ToolsFragment : Fragment() {

    private var _binding: FragmentToolsBinding? = null
    private val binding get() = _binding!!
    private var currentImageUri: Uri? = null
    private val scanViewModel: ScanViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize any necessary components here
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentToolsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cameraButton.setOnClickListener {
            startCamera()
        }

        binding.scanButton.setOnClickListener {

            val file = uriToFile(currentImageUri!!, requireContext())  // Use requireContext() here
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image", file.name, requestImageFile
            )

            binding.progressIndicator.visibility = View.VISIBLE
            scanViewModel.scan(imageMultipart).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        binding.progressIndicator.visibility = View.VISIBLE
                    }
                    is ResultState.Success -> {
                        binding.progressIndicator.visibility = View.GONE
                        Toast.makeText(requireContext(), "Scan successfull", Toast.LENGTH_SHORT).show()

                        val label = result.data.data?.label
                        val description = result.data.data?.description

                        val intent = Intent(requireContext(), ResultActivity::class.java)
                        intent.putExtra("label", label)
                        intent.putExtra("description", description)
                        intent.putExtra("imageUri", currentImageUri.toString())
                        startActivity(intent)
                    }
                    is ResultState.Error -> {
                        binding.progressIndicator.visibility = View.GONE
                        Toast.makeText(requireContext(), "Error: ${result.message}", Toast.LENGTH_LONG).show()
                        Log.e("ScanViewModel", "Error: ${result.message}")
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imageHolder.setImageURI(it)
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ToolsFragment()
    }
}
