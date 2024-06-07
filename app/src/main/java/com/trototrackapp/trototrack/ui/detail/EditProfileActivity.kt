package com.trototrackapp.trototrack.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.data.local.UserPreference
import com.trototrackapp.trototrack.databinding.ActivityEditProfileBinding
import com.trototrackapp.trototrack.ui.viewmodel.ProfileViewModel
import com.trototrackapp.trototrack.ui.viewmodel.ViewModelFactory

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()

            binding.progressIndicator.visibility = View.VISIBLE
            profileViewModel.updateProfile(name, username, email).observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        binding.progressIndicator.visibility = View.VISIBLE
                    }
                    is ResultState.Success -> {
                        binding.progressIndicator.visibility = View.GONE
                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, DetailAccountActivity::class.java)

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
}