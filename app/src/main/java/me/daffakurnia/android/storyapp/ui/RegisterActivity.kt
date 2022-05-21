package me.daffakurnia.android.storyapp.ui

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import me.daffakurnia.android.storyapp.api.ApiConfig
import me.daffakurnia.android.storyapp.databinding.ActivityRegisterBinding
import me.daffakurnia.android.storyapp.response.RegisterResponse
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        val titleTextView = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val nameTextView = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signupButton = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)
        val registerTextView = ObjectAnimator.ofFloat(binding.registerTextView, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)

        val name = AnimatorSet().apply {
            playTogether(nameTextView, nameEditTextLayout)
        }

        val email = AnimatorSet().apply {
            playTogether(emailTextView, emailEditTextLayout)
        }

        val password = AnimatorSet().apply {
            playTogether(passwordTextView, passwordEditTextLayout)
        }

        val login = AnimatorSet().apply {
            playTogether(registerTextView, btnLogin)
        }

        AnimatorSet().apply {
            playSequentially(titleTextView, name, email, password, signupButton, login)
            start()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val moveIntent = Intent(this, LoginActivity::class.java)
            val optionCombat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@RegisterActivity,
                    Pair(binding.imageView, "logo")
                )

            startActivity(moveIntent, optionCombat.toBundle())
        }

        binding.signupButton.setOnClickListener {
            if(registerValidation()) {
                val client = ApiConfig.getApiService().register(name, email, password)
                client.enqueue(object : Callback,
                    retrofit2.Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@RegisterActivity, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@RegisterActivity, "Registrasi Gagal", Toast.LENGTH_SHORT).show()
                            Log.e(TAG, "onFailure: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(this@RegisterActivity, "Registrasi Gagal", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "onFailure: ${t.message}")
                    }

                })
            }
        }
    }

    private fun registerValidation(): Boolean {
        with(binding) {
            name = nameEditText.text?.toString()?.trim()!!
            email = emailEditText.text?.toString()?.trim()!!
            password = passwordEditText.text?.toString()?.trim()!!
            val isValid: Boolean

            when {
                name.isEmpty() -> {
                    //nameEditText.error = resources.getString(R.string.title_register_activity_layout_1)
                    isValid = false
                }
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