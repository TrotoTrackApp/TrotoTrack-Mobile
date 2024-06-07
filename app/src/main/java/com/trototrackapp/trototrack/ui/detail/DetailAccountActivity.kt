package com.trototrackapp.trototrack.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.trototrackapp.trototrack.data.local.UserPreference
import com.trototrackapp.trototrack.databinding.ActivityDetailAccountBinding
import com.trototrackapp.trototrack.ui.welcome.WelcomeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailAccountActivity : AppCompatActivity() {


    private lateinit var binding: ActivityDetailAccountBinding
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userPreference = UserPreference.getInstance(this)

        binding.logout.setOnClickListener {
            showLogoutConfirmationDialog()
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
        CoroutineScope(Dispatchers.IO).launch {
            val name = userPreference.getName()
            val username = userPreference.getUsername()
            val email = userPreference.getEmail()

            withContext(Dispatchers.Main) {
                binding.nameUser.text = name
                binding.usernameUser.text = username
                binding.emailUser.text = email
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
