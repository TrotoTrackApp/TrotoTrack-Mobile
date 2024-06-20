package com.trototrackapp.trototrack.ui.add

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.content.pm.PackageManager
import android.database.Cursor
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.databinding.ActivityJobBinding
import com.trototrackapp.trototrack.ui.home.MainActivity
import com.trototrackapp.trototrack.ui.viewmodel.JobViewModel
import com.trototrackapp.trototrack.ui.viewmodel.ViewModelFactory
import com.trototrackapp.trototrack.util.uriToPdfFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject

class JobActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobBinding
    private val jobViewModel: JobViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private var currentFileUri: Uri? = null
    private var jobId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        jobId = intent.getStringExtra("id")

        binding.fileButton.setOnClickListener {
            openFilePicker()
        }

        binding.submitButton.setOnClickListener {
            if (currentFileUri == null) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val name = binding.nameEditText.text.toString()
            val nik = binding.nikEditText.text.toString()
            val address = binding.addressEditText.text.toString()
            val phone = binding.phoneEditText.text.toString()

            val nameRequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val nikRequestBody = nik.toRequestBody("text/plain".toMediaTypeOrNull())
            val addressRequestBody = address.toRequestBody("text/plain".toMediaTypeOrNull())
            val phoneRequestBody = phone.toRequestBody("text/plain".toMediaTypeOrNull())

            val file = uriToPdfFile(currentFileUri!!, this)
            val requestPdfFile = file.asRequestBody("application/pdf".toMediaTypeOrNull())
            val fileMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file", file.name, requestPdfFile
            )

            // Check if jobId exists and call the appropriate method
            if (jobId != null) {
                // Update existing job
                jobViewModel.updateJob(
                    jobId!!,
                    nameRequestBody,
                    nikRequestBody,
                    addressRequestBody,
                    phoneRequestBody,
                    fileMultipart
                ).observe(this) { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            binding.progressIndicator.visibility = View.VISIBLE
                        }
                        is ResultState.Success -> {
                            binding.progressIndicator.visibility = View.GONE
                            Toast.makeText(
                                this,
                                "Application has been successfully sent",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
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
                            val dialog = AlertDialog.Builder(this)
                                .setMessage(errorMessage)
                                .setPositiveButton("OK", null)
                                .create()
                            dialog.show()
                        }
                    }
                }
            } else {
                // Create new job
                jobViewModel.job(
                    nameRequestBody,
                    nikRequestBody,
                    addressRequestBody,
                    phoneRequestBody,
                    fileMultipart
                ).observe(this) { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            binding.progressIndicator.visibility = View.VISIBLE
                        }
                        is ResultState.Success -> {
                            binding.progressIndicator.visibility = View.GONE
                            Toast.makeText(
                                this,
                                "Application has been successfully sent",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
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
                            val dialog = AlertDialog.Builder(this)
                                .setMessage(errorMessage)
                                .setPositiveButton("OK", null)
                                .create()
                            dialog.show()
                        }
                    }
                }
            }
        }
    }

    private fun openFilePicker() {
        selectPdfLauncher.launch("application/pdf")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openFilePicker()
        }
    }

    private val selectPdfLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            currentFileUri = uri
            val fileName = getFileName(uri)
            binding.fileLocationEditText.setText(fileName)
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        result = it.getString(nameIndex)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result ?: "unknown_file"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
