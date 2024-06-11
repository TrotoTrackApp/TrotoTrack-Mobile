package com.trototrackapp.trototrack.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.trototrackapp.trototrack.databinding.ActivityDetailArticleBinding

class DetailArticleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val image = intent.getStringExtra("image")
        val date = intent.getStringExtra("date")

        binding.articleTitle.text = title
        binding.articleDescription.text = description
        binding.articleDate.text = date
        Glide.with(this)
            .load(image)
            .into(binding.articleImage)
    }
}