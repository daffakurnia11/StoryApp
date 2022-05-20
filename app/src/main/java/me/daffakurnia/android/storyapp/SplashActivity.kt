package me.daffakurnia.android.storyapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import me.daffakurnia.android.storyapp.databinding.ActivitySplashBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        val pref = AppDataStore.getInstance(dataStore)
        val authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
        authViewModel.loginToken().observe(this) { token: String? ->
            if (token != null || intent.getStringExtra(MainActivity.TOKEN) != null) {
                val moveIntent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(moveIntent)
                finish()
            } else {
                val moveIntent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(moveIntent)
                finish()
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        supportActionBar?.hide()
    }

}