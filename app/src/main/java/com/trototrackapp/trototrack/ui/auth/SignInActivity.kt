package com.trototrackapp.trototrack.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.databinding.ActivitySignInBinding
import com.trototrackapp.trototrack.ui.viewmodel.AuthViewModel
import com.trototrackapp.trototrack.ui.viewmodel.ViewModelFactory
import org.json.JSONException
import org.json.JSONObject

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val authViewModel: AuthViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.DoneButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val username = binding.usernameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirm_password = binding.passwordConfirmationEditText.text.toString()

            binding.progressIndicator.visibility = View.VISIBLE
            authViewModel.register(name, username, email, password, confirm_password).observe(this) { result ->
                when (result) {
                    is ResultState.Loading -> {
                        binding.progressIndicator.visibility = View.VISIBLE
                    }
                    is ResultState.Success -> {
                        binding.progressIndicator.visibility = View.GONE
                        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                    is ResultState.Error -> {
                        binding.progressIndicator.visibility = View.GONE
                        val errorMessage = result.message?.let {
                            try {
                                val json = JSONObject(it)
                                json.getString("message")
                            } catch (e: JSONException) {
                                it
                            }
                        } ?: "An error occurred"
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}