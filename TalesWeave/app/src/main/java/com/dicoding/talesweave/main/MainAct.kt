package com.dicoding.talesweave.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.talesweave.databinding.ActivityMainBinding
import com.dicoding.talesweave.StoryAdapter
import com.dicoding.talesweave.loginregist.LoginAct
import com.dicoding.talesweave.loginregist.WelcomeAct
import com.dicoding.talesweave.maps.MapsAct
import com.dicoding.talesweave.viewmodel.MainVM
import com.dicoding.talesweave.viewmodel.VMFactory
import com.dicoding.talesweave.response.ListStoryItem


class MainAct : AppCompatActivity() {
    private lateinit var adapter: StoryAdapter
    private val viewModel by viewModels<MainVM> {
        VMFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.Upload.setOnClickListener {
            val intent = Intent(this, AddStoryAct::class.java)
            startActivity(intent)

        }

        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rviewStory.layoutManager = layoutManager

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeAct::class.java))
                finish()
            } else {
                setStory()
            }

            binding.Mapsbtn.setOnClickListener {
                val intent = Intent(this, MapsAct::class.java)
                startActivity(intent)

            }
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
            val intent = Intent(this, LoginAct::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            finish()

        }

        setupView()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

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
    }

    private fun setStory() {
        adapter = StoryAdapter()
        showLoading(true)
        binding.rviewStory.adapter = adapter
        viewModel.storyPager.observe(this) { pagingData: PagingData<ListStoryItem> ->
            showLoading(false)
            adapter.submitData(lifecycle, pagingData)
        }
    }
}