package me.daffakurnia.android.storyapp.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import me.daffakurnia.android.storyapp.*
import me.daffakurnia.android.storyapp.data.AppDataStore
import me.daffakurnia.android.storyapp.data.AuthViewModel
import me.daffakurnia.android.storyapp.data.ViewModelFactory
import me.daffakurnia.android.storyapp.databinding.ActivityMainBinding

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            val layoutManager = GridLayoutManager(this, 2)
            binding.rvStories.layoutManager = layoutManager
        } else {
            val layoutManager = LinearLayoutManager(this)
            binding.rvStories.layoutManager = layoutManager
        }

        val pref = AppDataStore.getInstance(dataStore)
        val authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]
        authViewModel.loginToken().observe(this) { token: String? ->
            showLoading(true)
            val loginToken = "Bearer $token"
            getData(loginToken)
            showLoading(false)
        }
    }

    private fun getData(loginToken: String) {
        val adapter = ListStoriesAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        lifecycleScope.launch {
            mainViewModel.stories(loginToken).observe(this@MainActivity) {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.maps -> {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.settings -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.add_story -> {
                val intent = Intent(this@MainActivity, StoryActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return true
        }
    }

    companion object {
        const val TOKEN = "token"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
