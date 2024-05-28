package com.trototrackapp.trototrack.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.trototrackapp.trototrack.R
import com.trototrackapp.trototrack.databinding.ActivityWelcomeBinding
import com.trototrackapp.trototrack.ui.auth.LoginActivity
import com.trototrackapp.trototrack.ui.auth.SignInActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.LoginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.SignInButton.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }
}