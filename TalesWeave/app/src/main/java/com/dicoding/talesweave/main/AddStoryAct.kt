package com.dicoding.talesweave.main

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import android.Manifest
import android.view.View
import androidx.activity.viewModels
import com.dicoding.talesweave.data.UIState
import com.dicoding.talesweave.main.CameraAct.Companion.CAMERAX_RESULT
import com.dicoding.talesweave.databinding.ActivityUploadStoryBinding
import com.dicoding.talesweave.viewmodel.StoryVM
import com.dicoding.talesweave.viewmodel.VMFactory
import com.dicoding.talesweave.viewmodel.reduceFileImage
import com.dicoding.talesweave.viewmodel.uriToFile

class AddStoryAct : AppCompatActivity() {

    private val viewModel by viewModels<StoryVM> {
        VMFactory.getInstance(this)
    }
    private lateinit var binding: ActivityUploadStoryBinding

    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Sorry, permission request denied!", Toast.LENGTH_LONG).show()
            }
        }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Upload Story"

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.buttonGallery.setOnClickListener { startGallery() }
        binding.buttonCamera.setOnClickListener { startCameraX() }
        binding.buttonUpload.setOnClickListener { uploadImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraAct::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraAct.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.editDescription.text.toString()

            showLoading(true)
            viewModel.postImage(imageFile, description).observe(this) { data ->
                if (data != null) {
                    when (data) {
                        is UIState.Loading -> {
                            showLoading(isLoading = true)
                        }

                        is UIState.Success -> {
                            showLoading(isLoading = false)
                            val intent = Intent(this, MainAct::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }

                        is UIState.Error -> {
                            showLoading(isLoading = false)
                            showToast(data.error)

                        }
                    }
                }

            }

        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
