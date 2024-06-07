package com.trototrackapp.trototrack.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.trototrackapp.trototrack.data.ResultState
import com.trototrackapp.trototrack.data.local.UserPreference
import com.trototrackapp.trototrack.databinding.ActivityDetailAccountBinding
import com.trototrackapp.trototrack.ui.viewmodel.ProfileViewModel
import com.trototrackapp.trototrack.ui.viewmodel.ViewModelFactory
import com.trototrackapp.trototrack.ui.welcome.WelcomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailAccountActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDetailAccountBinding
    private lateinit var userPreference: UserPreference
    private val profileViewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userPreference = UserPreference.getInstance(this)

        binding.logout.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        binding.editButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        loadUserData()
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { dialog, which ->
                logout()
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            userPreference.clear()
        }
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loadUserData() {
        profileViewModel.getUserProfile().observe(this) { result ->
            when (result) {
                is ResultState.Loading -> {
                    binding.progressIndicator.visibility = View.VISIBLE
                }
                is ResultState.Success -> {
                    binding.progressIndicator.visibility = View.GONE
                    val userProfile = result.data.data
                    if (userProfile != null) {
                        binding.nameUser.text = userProfile.name
                        binding.usernameUser.text = userProfile.username
                        binding.emailUser.text = userProfile.email
                    }
                }
                is ResultState.Error -> {
                    binding.progressIndicator.visibility = View.GONE
                    Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_SHORT).show()
                }
            }
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
