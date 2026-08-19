package com.plantilla.base

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

class CameraManager(private val context: Context) {

    fun iniciarCamaras() {
        val activity = context as? Activity ?: return
        
        // Creamos un diseño en pantalla para mostrar la cámara
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        // Vista previa de la cámara (la ventana donde se verá el lente)
        val previewView = PreviewView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        }

        // Botón para regresar al menú principal
        val btnVolver = Button(context).apply {
            text = "⬅️ Volver al Menú"
            setOnClickListener {
                // Si la actividad extiende de MainActivity, podemos volver al menú
                if (activity is MainActivity) {
                    // Invocamos una función para recargar el menú principal
                    activity.recreate()
                }
            }
        }

        layout.addView(previewView)
        layout.addView(btnVolver)
        activity.setContentView(layout)

        // Vinculamos el ciclo de vida con CameraX para activar el lente trasero por ahora
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    activity as LifecycleOwner,
                    cameraSelector,
                    preview
                )
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(context))
    }
}
