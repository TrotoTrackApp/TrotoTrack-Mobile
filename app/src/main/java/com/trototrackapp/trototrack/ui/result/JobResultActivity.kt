package com.trototrackapp.trototrack.ui.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.trototrackapp.trototrack.databinding.ActivityJobResultBinding

class JobResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}