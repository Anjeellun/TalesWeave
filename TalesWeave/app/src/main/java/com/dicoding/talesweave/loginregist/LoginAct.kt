package com.dicoding.talesweave.loginregist

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.talesweave.data.UIState
import com.dicoding.talesweave.data.UsrModel
import com.dicoding.talesweave.databinding.ActivityLoginBinding
import com.dicoding.talesweave.main.MainAct
import com.dicoding.talesweave.viewmodel.LoginVM
import com.dicoding.talesweave.viewmodel.VMFactory

class LoginAct : AppCompatActivity() {

    private val viewModel by viewModels<LoginVM> {
        VMFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        binding.accountRegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterAct::class.java)
            startActivity(intent)
        }

        binding.forgotButton.setOnClickListener {
            val intent = Intent(this, ForgotPassAct::class.java)
            startActivity(intent)
        }
    }

    private fun setupView() {
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

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.login(email, password).observe(this) {
                if (it != null) {
                    when (it) {
                        is UIState.Loading -> {
                            showLoading(isLoading = true)

                        }

                        is UIState.Success -> {
                            AlertDialog.Builder(this).apply {
                                setTitle("Thank You For Waiting, Dear User!")
                                setMessage("Let's go in and share your wonderful stories! >_<")
                                setPositiveButton("I'm Ready.") { _, _ ->
                                    val data = UsrModel(
                                        it.data.loginResult.name ?: "",
                                        it.data.loginResult.userId ?: "",
                                        it.data.loginResult.token ?: "",
                                        true
                                    )
                                    viewModel.saveSession(data)
                                    val intent = Intent(context, MainAct::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }

                            showLoading(isLoading = false)
                        }

                        is UIState.Error -> {
                            showLoading(isLoading = false)
                            showToast(it.error)
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
