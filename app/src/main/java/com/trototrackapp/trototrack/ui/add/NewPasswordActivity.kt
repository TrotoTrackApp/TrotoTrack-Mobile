package com.trototrackapp.trototrack.ui.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.databinding.ActivityNewPasswordBinding
import com.trototrackapp.trototrack.ui.home.MainActivity
import com.trototrackapp.trototrack.ui.viewmodel.ForgetPasswordViewModel
import com.trototrackapp.trototrack.ui.viewmodel.ViewModelFactory
import org.json.JSONException
import org.json.JSONObject

class NewPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewPasswordBinding
    private val forgetPasswordViewModel: ForgetPasswordViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("email")

        binding.submitButton.setOnClickListener {
            val password = binding.passwordEditText.text.toString()
            val passwordConfirmation = binding.passwordConfirmationEditText.text.toString()
            if (email != null) {
                forgetPasswordViewModel.newPassword(email, password, passwordConfirmation).observe(this) { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            binding.progressIndicator.visibility = View.VISIBLE
                        }

                        is ResultState.Success -> {
                            binding.progressIndicator.visibility = View.GONE
                            val dialog = AlertDialog.Builder(this)
                                .setMessage("New password created successfully")
                                .setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finishAffinity()
                                }
                                .create()
                            dialog.show()
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
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}