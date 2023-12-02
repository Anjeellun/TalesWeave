package com.dicoding.talesweave.loginregist

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.dicoding.talesweave.data.UserPreference
import com.dicoding.talesweave.data.dataStore
import com.dicoding.talesweave.databinding.ActivityWelcomeBinding
import com.dicoding.talesweave.main.MainAct
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class WelcomeAct : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
        setUpActions()
        playAnimation()

        userPreference = UserPreference.getInstance(dataStore)

        runBlocking {
            checkLoginStatus()
        }
    }

    private fun setUpView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setUpActions() {
        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterAct::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val intent = Intent(this, LoginAct::class.java)
            startActivity(intent)
        }
    }

    private suspend fun checkLoginStatus() {
        val user = userPreference.getSession().first() // Get user session data

        if (user.isLogin) {
            val intent = Intent(this@WelcomeAct, MainAct::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.logoWelcome, View.TRANSLATION_X, -25f, 25f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }
    }
}
