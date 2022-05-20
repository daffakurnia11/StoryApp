package me.daffakurnia.android.storyapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import me.daffakurnia.android.storyapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val list = ArrayList<Stories>()

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
        val authViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            AuthViewModel::class.java
        )
        authViewModel.loginToken().observe(this) { token: String? ->
            getAllStories(token)
            if (token != null || intent.getStringExtra(TOKEN) != null) {
                getAllStories(token)
            } else {
                val moveIntent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(moveIntent)
                finish()
            }
        }
    }

    private fun getAllStories(token: String?) {
        val bearerToken = HashMap<String, String>()
        bearerToken["Authorization"] = "Bearer $token"

        val client = ApiConfig.getApiService().getAllStories(bearerToken)
        client.enqueue(object : Callback, retrofit2.Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        getStories(responseBody.listStory)
                    }
                } else {
                    Log.e(this@MainActivity.toString(), "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Log.e(this@MainActivity.toString(), "onFailure: ${t.message}")
            }

        })
    }

    private fun getStories(listStory: List<ListStoryItem?>?) {
        val storyList = ArrayList<Stories>()

        if (listStory != null) {
            for (item in listStory) {
                storyList.add(
                    Stories(
                        item?.photoUrl,
                        item?.name,
                        item?.description
                    )
                )
            }
        }
        val adapter = ListStoriesAdapter(storyList)
        binding.rvStories.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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

}
