package com.trototrackapp.trototrack.ui.add

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.databinding.ActivityAddReportBinding
import com.trototrackapp.trototrack.ui.home.MainActivity
import com.trototrackapp.trototrack.ui.viewmodel.AddReportViewModel
import com.trototrackapp.trototrack.ui.viewmodel.ViewModelFactory
import com.trototrackapp.trototrack.util.getImageUri
import com.trototrackapp.trototrack.util.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.FileOutputStream

class AddReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddReportBinding
    private val addReportViewModel: AddReportViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    companion object {
        const val REQUEST_MAP = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val label = intent.getStringExtra("label")
        val imageUriString = intent.getStringExtra("imageUri")

        Glide.with(this)
            .load(imageUriString)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.imageHolder)

        val isFromMapActivity = intent.getBooleanExtra("FROM_MAP_ACTIVITY", false)
        if (isFromMapActivity) {
            val locationName = intent.getStringExtra("LOCATION_NAME")
            binding.mapLocationEditText.setText(locationName ?: "")
        }

        binding.mapButton.setOnClickListener {
            startActivityForResult(Intent(this, MapsActivity::class.java), REQUEST_MAP)
        }

        binding.submitReportButton.setOnClickListener{
            val location = binding.mapLocationEditText.text.toString()
            val reference_location = binding.addressEditText.text.toString()
            val latitude = intent.getDoubleExtra("LONGITUDE", 0.0)
            val longitude = intent.getDoubleExtra("LONGITUDE", 0.0)
            val statusDamage = label ?: "DefaultStatusDamage"
            val description = binding.descriptionEditTextLayout.text.toString()

            val imageUri = Uri.parse(imageUriString)
            val file = uriToFile(imageUri, this)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val image: MultipartBody.Part = MultipartBody.Part.createFormData(
                "image", file.name, requestImageFile
            )

            binding.progressIndicator.visibility = View.VISIBLE
            addReportViewModel.addReport(location, reference_location, latitude, longitude, image, statusDamage, description)
                .observe(this) { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            binding.progressIndicator.visibility = View.VISIBLE
                        }
                        is ResultState.Success -> {
                            binding.progressIndicator.visibility = View.GONE
                            Toast.makeText(this, "Report added successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        }
                        is ResultState.Error -> {
                            binding.progressIndicator.visibility = View.GONE
                            Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_MAP) {
            if (resultCode == Activity.RESULT_OK) {
                val locationName = data?.getStringExtra("LOCATION_NAME")
                binding.mapLocationEditText.setText(locationName ?: "")
            }
        }
    }
}
