package com.trototrackapp.trototrack.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.trototrackapp.trototrack.databinding.ActivityJobBinding
import com.trototrackapp.trototrack.ui.viewmodel.JobViewModel
import com.trototrackapp.trototrack.ui.viewmodel.ViewModelFactory

class JobActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobBinding
    private val jobViewModel: JobViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}