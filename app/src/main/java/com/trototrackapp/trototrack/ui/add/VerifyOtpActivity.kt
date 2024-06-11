package com.trototrackapp.trototrack.ui.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.trototrackapp.trototrack.R
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.databinding.ActivitySendOtpBinding
import com.trototrackapp.trototrack.databinding.ActivityVerifyOtpBinding
import com.trototrackapp.trototrack.ui.home.MainActivity
import com.trototrackapp.trototrack.ui.viewmodel.ForgetPasswordViewModel
import com.trototrackapp.trototrack.ui.viewmodel.ViewModelFactory
import org.json.JSONException
import org.json.JSONObject

class VerifyOtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyOtpBinding
    private val forgetPasswordViewModel: ForgetPasswordViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra("email")

        binding.verifyOtpButton.setOnClickListener {
            val otp = binding.otpEditText.text.toString()
            if (email != null) {
                forgetPasswordViewModel.verifyOtp(email, otp).observe(this) { result ->
                    when (result) {
                        is ResultState.Loading -> {
                            binding.progressIndicator.visibility = View.VISIBLE
                        }

                        is ResultState.Success -> {
                            binding.progressIndicator.visibility = View.GONE
                            val dialog = AlertDialog.Builder(this)
                                .setMessage("OTP verified successfully")
                                .setPositiveButton("OK") { dialog, _ ->
                                    dialog.dismiss()
                                    val intent = Intent(this, MainActivity::class.java).apply {
                                        putExtra("email", email)
                                    }
                                    startActivity(intent)
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