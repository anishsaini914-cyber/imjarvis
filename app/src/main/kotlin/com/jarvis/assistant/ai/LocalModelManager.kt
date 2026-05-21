package com.jarvis.assistant.ai

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalModelManager @Inject constructor(@ApplicationContext private val ctx: Context) {
    private val dir get() = File(ctx.filesDir, "local_models").also { it.mkdirs() }

    data class ModelInfo(val name: String, val fileName: String, val path: String, val sizeBytes: Long, val format: String) {
        val sizeFormatted get() = "%.1f MB".format(sizeBytes / (1024.0 * 1024.0))
    }

    fun getModels(): List<ModelInfo> = dir.listFiles()?.filter { it.extension in setOf("gguf", "ggml", "bin") }?.map {
        ModelInfo(it.nameWithoutExtension, it.name, it.absolutePath, it.length(), it.extension.uppercase())
    }?.sortedByDescending { it.sizeBytes } ?: emptyList()

    fun importModel(path: String): Result<ModelInfo> = try {
        val src = File(path)
        if (!src.exists()) return Result.failure(Exception("File not found"))
        val dst = File(dir, src.name)
        if (dst.exists()) return Result.failure(Exception("Already imported"))
        src.copyTo(dst)
        Result.success(ModelInfo(dst.nameWithoutExtension, dst.name, dst.absolutePath, dst.length(), dst.extension.uppercase()))
    } catch (e: Exception) { Result.failure(e) }

    fun deleteModel(name: String) = File(dir, name).delete()
}
