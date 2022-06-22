package me.daffakurnia.android.storyapp.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import me.daffakurnia.android.storyapp.data.AppDataStore
import me.daffakurnia.android.storyapp.data.AuthViewModel
import me.daffakurnia.android.storyapp.R
import me.daffakurnia.android.storyapp.data.ViewModelFactory
import me.daffakurnia.android.storyapp.databinding.ActivitySettingsBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = resources.getString(R.string.settings)

        binding.apply {
            logoutMenu.setOnClickListener {
                val pref = AppDataStore.getInstance(dataStore)
                val authViewModel = ViewModelProvider(
                    this@SettingsActivity,
                    ViewModelFactory(pref)
                )[AuthViewModel::class.java]
                authViewModel.clearToken()
                startActivity(Intent(this@SettingsActivity, LoginActivity::class.java))
                ActivityCompat.finishAffinity(this@SettingsActivity)
                finish()
            }
        }
    }
}