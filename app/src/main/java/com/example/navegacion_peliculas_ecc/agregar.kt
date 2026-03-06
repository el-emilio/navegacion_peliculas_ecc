package com.example.navegacion_peliculas_ecc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database

class agregar : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var ponernombre: EditText
    lateinit var ponergenero: EditText
    lateinit var poneranio: EditText
    val database = Firebase.database

    val myRef = database.getReference("peliculas")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar)

        //No se a que le moví que el id de setOnApplyWindowInsetsListener es eliminar, solo quería crear un boton pero asi se quedó. NO TOCAR

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.eliminar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ponernombre = findViewById(R.id.ponernombre)
        ponergenero = findViewById(R.id.ponergenero)
        poneranio = findViewById(R.id.poneranio)

        val guardar = findViewById<Button>(R.id.guardar)
        val cancelar = findViewById<Button>(R.id.cancelar)

        guardar.setOnClickListener {
            val ponernombre = ponernombre.text.toString()
            val ponergenero = ponergenero.text.toString()
            val poneranio = poneranio.text.toString()

            val pelicula = pelicula(ponernombre, poneranio, ponergenero)
            myRef.push().setValue(pelicula).addOnCompleteListener {
                task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Pelicula agregada", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, Home::class.java))
                }
            }

        }

        cancelar.setOnClickListener {
            ponernombre.setText("")
            ponergenero.setText("")
            poneranio.setText("")

            startActivity(Intent(this, Home::class.java))
        }



    }
}