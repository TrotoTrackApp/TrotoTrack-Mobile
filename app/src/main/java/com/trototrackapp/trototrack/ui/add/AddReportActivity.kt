package com.trototrackapp.trototrack.ui.add

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.trototrackapp.trototrack.databinding.ActivityAddReportBinding
import com.trototrackapp.trototrack.databinding.ActivityLoginBinding
import com.trototrackapp.trototrack.ui.auth.SignInActivity

class AddReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mapButton.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }
}