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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class detalle : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var nombreactual: EditText
    lateinit var generoactual: EditText
    lateinit var anioactual: EditText

    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.eliminar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("peliculas")

        nombreactual = findViewById(R.id.nombreactual)
        generoactual = findViewById(R.id.generoactual)
        anioactual = findViewById(R.id.anioactual)

        val editar = findViewById<Button>(R.id.editar)
        val cancelar = findViewById<Button>(R.id.cancelare)
        val eliminar = findViewById<Button>(R.id.eliminare)

        val nombre = intent.getStringExtra("nombre") ?: ""
        val genero = intent.getStringExtra("genero") ?: ""
        val anio = intent.getStringExtra("anio") ?: ""

        id = intent.getStringExtra("id")

        nombreactual.setText(nombre)
        generoactual.setText(genero)
        anioactual.setText(anio)

        editar.setOnClickListener {
            val nuevonombre = nombreactual.text.toString()
            val nuevogenero = generoactual.text.toString()
            val nuevoanio = anioactual.text.toString()

            if (nuevonombre.isEmpty() || nuevogenero.isEmpty() || nuevoanio.isEmpty()) {
                Toast.makeText(this, "Pero pon todo porfa", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (id != null) {
                println("ID antes de actualizar: $id")
                actualizacion(id!!, nuevonombre, nuevogenero, nuevoanio)
            }
        }

        cancelar.setOnClickListener {
            startActivity(Intent(this, Home::class.java))
        }

        eliminar.setOnClickListener {
            if (id != null) {
                myRef.child(id!!).removeValue()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Película eliminada", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, Home::class.java))
                        }
                    }
            }
        }
    }

    fun actualizacion(id: String, nombre: String, genero: String, anio: String){
        val nuevosDatos = mapOf(
            "nombre" to nombre,
            "genero" to genero,
            "anio" to anio
        )

        myRef.child(id).updateChildren(nuevosDatos)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Ya quedó", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, Home::class.java))
                }
            }
    }
}

