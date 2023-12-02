package com.dicoding.talesweave.loginregist

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.dicoding.talesweave.data.UIState
import com.dicoding.talesweave.databinding.ActivityRegisterBinding
import com.dicoding.talesweave.viewmodel.RegisterVM
import com.dicoding.talesweave.viewmodel.VMFactory

class RegisterAct : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterVM> {
        VMFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpView()
        setUpAction()
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

    private fun setUpAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            showLoading(true)

            viewModel.signup(name, email, password).observe(this) { uiState ->
                when (uiState) {
                    is UIState.Loading -> {

                    }

                    is UIState.Success<*> -> {

                        showLoading(false)

                        AlertDialog.Builder(this).apply {
                            setTitle("Welcome, Dear User.")
                            setMessage("You have successfully Register. Let's share enjoyable stories together!!")
                            setPositiveButton("LET'S GOOOOO!") { _, _ ->
                                val intent = Intent(this@RegisterAct, LoginAct::class.java)
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                    }

                    is UIState.Error -> {

                        showLoading(false)

                        showToast(uiState.error)
                    }

                    else -> {

                    }
                }
            }
        }

        binding.accountLoginButton.setOnClickListener {
            val intent = Intent(this, LoginAct::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) = binding.progressBar.isVisible == isLoading

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
