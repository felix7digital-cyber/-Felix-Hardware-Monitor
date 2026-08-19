    private fun mostrarMenuPrincipal() {
        desregistrarSensores()

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(50, 80, 50, 50)
        }

        val titulo = TextView(this).apply {
            text = "📱 Félix Hardware Monitor v2.1"
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

        // Nueva opción para la futura herramienta de cámara dual
        val btnCamaraDual = Button(this).apply {
            text = "🎥 Cámara Dual (Próximamente)"
            setOnClickListener { 
                Toast.makeText(this@MainActivity, "Módulo de cámara dual en desarrollo...", Toast.LENGTH_SHORT).show() 
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
