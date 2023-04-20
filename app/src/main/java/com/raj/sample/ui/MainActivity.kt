package com.raj.sample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.raj.sample.R
import com.raj.sample.databinding.ActivityMainBinding
import com.raj.sample.util.FileUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var isChoose = true

    private lateinit var getContent: ActivityResultLauncher<String>
    private var selectedFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLaunchers()
        subscribeUpload()

        binding.upload.setOnClickListener {
            if (isChoose) {
                getContent.launch("image/*")
            } else {
                selectedFile?.let {
                    viewModel.test(it)
                    isChoose = true
                    binding.upload.text = "choose"
                }
            }
        }

    }

    private fun subscribeUpload() = lifecycleScope.launchWhenStarted {
        viewModel.setupEvent.collect { event ->
            /* when (event) {
                 "done" -> {
                     Toast.makeText(this@MainActivity, event, Toast.LENGTH_LONG).show()
                 }

                 "not_done" -> {
                     Toast.makeText(this@MainActivity, event, Toast.LENGTH_LONG).show()
                 }
             }*/
        }
    }

    private fun initLaunchers() {
        getContent = registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            if (uri != null) {
                binding.upload.text = "upload"
                isChoose = false
                selectedFile = FileUtil().getRealPathFromURI(this, uri)
            }
        }
    }

}