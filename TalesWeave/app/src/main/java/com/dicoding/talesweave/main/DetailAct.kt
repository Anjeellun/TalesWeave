package com.dicoding.talesweave.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dicoding.talesweave.databinding.ActivityDetailStoryBinding

class DetailAct : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra(EXTRA_NAME)
        val description = intent.getStringExtra(EXTRA_DESCRIPTION)
        val imgPhoto = intent.getStringExtra(EXTRA_PHOTO)

        supportActionBar?.title = "The Story Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tvName.text = name
        Glide.with(this).load(imgPhoto).into(binding.imgStory)
        binding.tvDescription.text = description

    }

    companion object {
        const val EXTRA_PHOTO = "extraPhoto"
        const val EXTRA_NAME = "extraName"
        const val EXTRA_DESCRIPTION = "extraDescription"
    }
}


