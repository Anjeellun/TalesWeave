package com.dicoding.talesweave.loginregist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.dicoding.talesweave.databinding.ActivityForgotPasswordBinding

// Sedang Iseng dan coba membuat Forgot Password menggunakan Firebase //
// Tidak ada sangkutannya dengan submission inti - Anjeli //

class ForgotPassAct : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.forgotButton.setOnClickListener {
            val email = binding.edForgotEmail.text.toString()
            if (email.isNotEmpty()) {
                binding.progressBar.visibility = View.VISIBLE
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        binding.progressBar.visibility = View.GONE
                        if (task.isSuccessful) {
                            showToast("Password reset email has been sent. Please check your inbox!")
                            finish()
                        } else {
                            showToast("Failed to send password reset email. Please ensure your email is correct.")
                        }
                    }
            } else {
                showToast("Please enter your email address.")
            }
        }

        binding.backToLoginButton.setOnClickListener {
            val intent = Intent(this, LoginAct::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
