package com.trototrackapp.trototrack.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.data.local.UserPreference
import com.trototrackapp.trototrack.databinding.ActivityLoginBinding
import com.trototrackapp.trototrack.ui.home.MainActivity
import com.trototrackapp.trototrack.ui.viewmodel.AuthViewModel
import com.trototrackapp.trototrack.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(this)

        binding.LoginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            binding.LoginButton.setOnClickListener {
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                binding.progressIndicator.visibility = View.VISIBLE
                authViewModel.login(email, password).observe(this) { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            binding.progressIndicator.visibility = View.VISIBLE
                        }
                        is ResultState.Success -> {
                            binding.progressIndicator.visibility = View.GONE
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                            val userData = result.data.data
                            if (userData != null) {
                                lifecycleScope.launch {
                                    userPreference.saveData(
                                        userData.id ?: "",
                                        userData.token ?: ""
                                    )
                                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                                    finishAffinity()
                                }
                            }
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
}