package com.plantilla.base

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat

class CameraManager(private val context: Context) {

    fun iniciarCamaras() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            // Aquí es donde configuraremos los dos sensores:
            // 1. CameraSelector.DEFAULT_BACK_CAMERA
            // 2. CameraSelector.DEFAULT_FRONT_CAMERA
            // Y los vincularemos al ciclo de vida de tu app
        }, ContextCompat.getMainExecutor(context))
    }
}
