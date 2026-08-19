package com.plantilla.base

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val textView = TextView(this).apply {
            text = "🚀 ¡Software de Alto Nivel Creado Desde el Celular!\n\nBienvenido a mi nueva aplicación independiente."
            textSize = 24f
            gravity = Gravity.CENTER
            setPadding(50, 100, 50, 50)
        }
        
        setContentView(textView)
    }
}
