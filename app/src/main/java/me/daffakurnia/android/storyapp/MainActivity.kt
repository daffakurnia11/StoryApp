package me.daffakurnia.android.storyapp

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowInsets
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import me.daffakurnia.android.storyapp.databinding.ActivityLoginBinding
import me.daffakurnia.android.storyapp.databinding.ActivityMainBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = AppDataStore.getInstance(dataStore)
        val authViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            AuthViewModel::class.java
        )
        authViewModel.loginToken().observe(this) { token: String? ->
            //binding.textView.text = token
            if (token != null || intent.getStringExtra(TOKEN) != null) {
                //binding.textView.text = token
            } else {
                val moveIntent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(moveIntent)
                finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, MenuFragment())
//                    .addToBackStack(null)
//                    .commit()
                return true
            }
            R.id.logout -> {
                val pref = AppDataStore.getInstance(dataStore)
                val authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
                authViewModel.clearToken()
                finish()
                return true
            }
            else -> return true
        }
    }

    companion object {
        const val TOKEN = "token"
    }

}