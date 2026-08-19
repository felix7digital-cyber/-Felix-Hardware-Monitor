package com.plantilla.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var acelerometro: Sensor? = null
    private var txtSensores: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        acelerometro = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        mostrarMenuPrincipal()
    }

    private fun mostrarMenuPrincipal() {
        desregistrarSensores()

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(50, 80, 50, 50)
        }

        val titulo = TextView(this).apply {
            text = "📱 Félix Hardware Monitor v3.0"
            textSize = 24f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 10)
        }

        val subtitulo = TextView(this).apply {
            text = "Centro de Diagnóstico y Herramientas"
            textSize = 14f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 50)
        }

        val btnBateriaSensores = Button(this).apply {
            text = "🔋 Batería y Sensores"
            setOnClickListener { mostrarPantallaBateriaYSensores() }
        }

        val btnInfo = Button(this).apply {
            text = "📊 Info del Dispositivo"
            setOnClickListener { mostrarPantallaInfo() }
        }

        val btnNotas = Button(this).apply {
            text = "📝 Notas en Memoria Local"
            setOnClickListener { mostrarPantallaNotas() }
        }

        val btnCamaraDual = Button(this).apply {
            text = "🎥 Cámara Dual"
            setOnClickListener { 
                val cameraManager = CameraManager(this@MainActivity)
                cameraManager.iniciarCamaras()
                Toast.makeText(this@MainActivity, "Abriendo motor de cámara dual...", Toast.LENGTH_SHORT).show() 
            }
        }

        layout.addView(titulo)
        layout.addView(subtitulo)
        layout.addView(btnBateriaSensores)
        layout.addView(btnInfo)
        layout.addView(btnNotas)
        layout.addView(btnCamaraDual)

        setContentView(layout)
    }

    private fun mostrarPantallaBateriaYSensores() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 50, 50, 50)
        }

        val titulo = TextView(this).apply {
            text = "🔋 Estado de Hardware"
            textSize = 22f
            setPadding(0, 0, 0, 30)
        }

        val intentBateria = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val nivel = intentBateria?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val escala = intentBateria?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val pctBateria = if (escala > 0) (nivel / escala.toFloat() * 100).toInt() else 0
        
        val tempRaw = intentBateria?.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) ?: 0
        val tempCelsius = tempRaw / 10.0

        val estadoCarga = intentBateria?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        val cargando = estadoCarga == BatteryManager.BATTERY_STATUS_CHARGING || estadoCarga == BatteryManager.BATTERY_STATUS_FULL

        val txtBateria = TextView(this).apply {
            text = "• Carga: $pctBateria%\n• Estado: ${if (cargando) "Cargando ⚡" else "Desconectado 🔋"}\n• Temperatura: $tempCelsius °C"
            textSize = 16f
            setPadding(0, 0, 0, 30)
        }

        txtSensores = TextView(this).apply {
            text = "Mueve el teléfono..."
            textSize = 15f
        }

        val btnVolver = Button(this).apply {
            text = "⬅️ Volver"
            setOnClickListener { mostrarMenuPrincipal() }
        }

        layout.addView(titulo)
        layout.addView(txtBateria)
        layout.addView(txtSensores)
        layout.addView(btnVolver)

        setContentView(layout)

        acelerometro?.let {
            sensorManager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            txtSensores?.text = "📐 Movimiento:\nX: %.2f\nY: %.2f\nZ: %.2f".format(x, y, z)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    private fun desregistrarSensores() { sensorManager?.unregisterListener(this) }

    private fun mostrarPantallaInfo() {
        desregistrarSensores()
        val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(50, 50, 50, 50) }
        val info = TextView(this).apply {
            text = "Modelo: ${Build.MODEL}\nMarca: ${Build.MANUFACTURER}\nAndroid: ${Build.VERSION.RELEASE}\nApp: v3.0"
            textSize = 16f
        }
        val btnVolver = Button(this).apply { text = "⬅️ Volver"; setOnClickListener { mostrarMenuPrincipal() } }
        layout.addView(info); layout.addView(btnVolver)
        setContentView(layout)
    }

    private fun mostrarPantallaNotas() {
        desregistrarSensores()
        val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(50, 50, 50, 50) }
        val input = EditText(this).apply { hint = "Nota..." }
        val btnGuardar = Button(this).apply { text = "Guardar"; setOnClickListener { Toast.makeText(this@MainActivity, "Guardado", Toast.LENGTH_SHORT).show() } }
        val btnVolver = Button(this).apply { text = "⬅️ Volver"; setOnClickListener { mostrarMenuPrincipal() } }
        layout.addView(input); layout.addView(btnGuardar); layout.addView(btnVolver)
        setContentView(layout)
    }

    override fun onPause() { super.onPause(); desregistrarSensores() }
}
