package me.daffakurnia.android.storyapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import me.daffakurnia.android.storyapp.data.AppDataStore
import me.daffakurnia.android.storyapp.data.AuthViewModel
import me.daffakurnia.android.storyapp.data.ViewModelFactory
import me.daffakurnia.android.storyapp.api.ApiConfig
import me.daffakurnia.android.storyapp.databinding.ActivityLoginBinding
import me.daffakurnia.android.storyapp.response.LoginResponse
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = AppDataStore.getInstance(dataStore)
        val authViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AuthViewModel::class.java]

        setupView()
        setupAction(authViewModel)
        playAnimation()
    }

    @SuppressLint("Recycle")
    private fun playAnimation() {
        val titleTextView = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val messageTextView = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val loginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val registerTextView = ObjectAnimator.ofFloat(binding.registerTextView, View.ALPHA, 1f).setDuration(500)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        val titleText = AnimatorSet().apply {
            playTogether(titleTextView, messageTextView)
        }

        val email = AnimatorSet().apply {
            playTogether(emailTextView, emailEditTextLayout)
        }

        val password = AnimatorSet().apply {
            playTogether(passwordTextView, passwordEditTextLayout)
        }

        val register = AnimatorSet().apply {
            playTogether(registerTextView, btnRegister)
        }

        AnimatorSet().apply {
            playSequentially(titleText, email, password, loginButton, register)
            start()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        supportActionBar?.hide()
    }

    private fun setupAction(authViewModel: AuthViewModel) {
        binding.btnRegister.setOnClickListener {
            val moveIntent = Intent(this, RegisterActivity::class.java)
            val optionCombat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@LoginActivity,
                    Pair(binding.imageView, "logo")
                )

            startActivity(moveIntent, optionCombat.toBundle())
        }
        binding.loginButton.setOnClickListener {
            if(loginValidation()) {
                val client = ApiConfig.getApiService().login(email, password)
                client.enqueue(object : Callback,
                    retrofit2.Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            val tokenLogin = responseBody?.loginResult?.token
                            tokenLogin?.let { token ->
                                authViewModel.saveToken(token)
                            }
                            Toast.makeText(this@LoginActivity, "Login Berhasil", Toast.LENGTH_SHORT).show()

                            val moveIntent = Intent(this@LoginActivity, MainActivity::class.java)
                            moveIntent.putExtra(MainActivity.TOKEN, tokenLogin)
                            startActivity(moveIntent)
                        } else {
                            Toast.makeText(this@LoginActivity, "Login tidak berhasil", Toast.LENGTH_SHORT).show()
                            Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Login Gagal", Toast.LENGTH_SHORT).show()
                        Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                    }

                })
            }
        }
    }

    private fun loginValidation(): Boolean {
        with(binding) {
            email = emailEditText.text?.toString()?.trim()!!
            password = passwordEditText.text?.toString()?.trim()!!
            val isValid: Boolean

            when {
                email.isEmpty() -> {
                    //emailEditText.error = resources.getString(R.string.title_register_activity_layout_2)
                    isValid = false
                }
                password.isEmpty() -> {
                    //passwordEditText.error = resources.getString(R.string.title_register_activity_layout_3)
                    isValid = false
                }
                else -> {
                    isValid = true
                }
            }
            return isValid
        }
    }
}