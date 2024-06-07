package com.trototrackapp.trototrack.ui.result

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.trototrackapp.trototrack.R
import com.trototrackapp.trototrack.databinding.ActivityAddReportBinding
import com.trototrackapp.trototrack.databinding.ActivityResultBinding
import com.trototrackapp.trototrack.ui.add.AddReportActivity
import com.trototrackapp.trototrack.ui.add.MapsActivity
import com.trototrackapp.trototrack.ui.home.MainActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val label = intent.getStringExtra("label")
        val description = intent.getStringExtra("description")
        val imageUriString = intent.getStringExtra("imageUri")

        binding.resultLabel.text = label
        binding.description.text = description

        val imageUri = Uri.parse(imageUriString)
        binding.resultImage.setImageURI(imageUri)

        val backgroundColor = when (label) {
            "light damaged" -> Color.YELLOW
            "heavy damaged" -> Color.RED
            "good" -> Color.GREEN
            else -> Color.TRANSPARENT
        }

        binding.resultLabel.backgroundTintList = ColorStateList.valueOf(backgroundColor)

        binding.addReportButton.setOnClickListener {
            val intent = Intent(this, AddReportActivity::class.java)
            intent.putExtra("label", label)
            intent.putExtra("imageUri", imageUriString)
            startActivity(intent)
        }

        binding.backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}