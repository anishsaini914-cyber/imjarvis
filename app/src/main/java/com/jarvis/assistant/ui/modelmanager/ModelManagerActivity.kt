package com.jarvis.assistant.ui.modelmanager

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.assistant.databinding.ActivityModelManagerBinding
import com.jarvis.assistant.domain.model.LocalModel
import com.jarvis.assistant.domain.model.ModelFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModelManagerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModelManagerBinding
    private val models = mutableListOf<LocalModel>()

    private val filePicker = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
        uri?.let { importModel(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityModelManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnImport.setOnClickListener { filePicker.launch(arrayOf("application/octet-stream", "*/*")) }
    }

    private fun importModel(uri: Uri) {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val name = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                val size = it.getLong(it.getColumnIndexOrThrow(OpenableColumns.SIZE))
                val format = when {
                    name.endsWith(".gguf") -> ModelFormat.GGUF
                    name.endsWith(".ggml") -> ModelFormat.GGML
                    else -> { Toast.makeText(this, "Unsupported format", Toast.LENGTH_SHORT).show(); return }
                }
                models.add(LocalModel(id = uri.toString(), name = name, path = uri.toString(), format = format, sizeBytes = size))
                updateUI()
                Toast.makeText(this, "Imported: $name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI() {
        binding.emptyState.visibility = if (models.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        binding.modelsRecyclerView.visibility = if (models.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE
    }
}
