package com.example.practica6

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    // Declaración de variables para los elementos de la interfaz
    lateinit var cdato: EditText  // Campo de texto donde el usuario escribe
    lateinit var cinfo: TextView // Texto donde se muestra la info leída del archivo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Se obtienen las referencias a los elementos del layout
        cdato = findViewById<EditText>(R.id.gdato)
        cinfo = findViewById<TextView>(R.id.ginfo)
    }

    // Función que guarda texto en un archivo
    fun guardar(texto: String){
        try {
            // Obtiene la ruta de almacenamiento externo de la app
            val rutaSD = baseContext.getExternalFilesDir(null)?.absolutePath
            val carpeta = File(rutaSD, "carpeta") // Crea referencia a la carpeta

            // Si la carpeta no existe, la crea
            if(!carpeta.exists()){
                carpeta.mkdir()
            }

            // Crea o abre el archivo 'datos.txt' dentro de esa carpeta
            val archivoFisico = File(carpeta, "datos.txt")

            // Agrega el texto al archivo, seguido de un salto de línea
            archivoFisico.appendText("$texto \n")
        }catch (e: Exception){
            // Muestra un mensaje si hay error al guardar
            Toast.makeText(this, "No se pudo guardar", Toast.LENGTH_SHORT).show()
        }
    }

    // Función que lee todo el contenido del archivo y lo devuelve como String
    fun leer(): String{
        var texto = ""
        try {
            // Ruta y archivo como en 'guardar()'
            val rutaSD = baseContext.getExternalFilesDir(null)?.absolutePath
            val carpeta = File(rutaSD, "carpeta")
            val archivoFisico = File(carpeta, "datos.txt")

            // Se abre el archivo y se lee todo su contenido
            val archivo = BufferedReader(
                InputStreamReader(FileInputStream(archivoFisico))
            )

            // El uso de 'use' asegura que el archivo se cierre automáticamente
            texto = archivo.use (BufferedReader::readText)
        }catch (e: Exception){
            // Si algo falla, se avisa con un Toast
            Toast.makeText(this, "No se pudo leer", Toast.LENGTH_SHORT).show()
        }
        return texto // Se retorna el texto leído
    }

    // Función que se ejecuta cuando el usuario da clic al botón (desde XML)
    fun aceptar(vista: View) {
        val texto = cdato.text.toString() // Obtiene el texto que el usuario escribió
        guardar(texto)                    // Lo guarda en el archivo
        cinfo.text = leer()              // Luego lee todo el archivo y lo muestra en pantalla
    }
}