package com.example.navegacion_peliculas_ecc

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    //private var contentView by Delegates.notNull<Int>()
    private lateinit var auth: FirebaseAuth

    lateinit var correo: EditText
    lateinit var contrasena: EditText



    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        correo = findViewById(R.id.correo)
        contrasena = findViewById(R.id.contrasena)

    }

    //sandwich@tomates.com
    //embutidos

    fun login(view: View){

        val cor = correo.text.toString()
        val con = contrasena.text.toString()

        if(cor!="" && con!=""){
            auth.signInWithEmailAndPassword(cor,con ).addOnCompleteListener { task ->

                if(task.isSuccessful){
                    Toast.makeText(this, "Login Exitoso", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, Home::class.java).putExtra("email", task.result.user?.email.toString()))//Cambiar de aquí a "Home" que es una clase java
                    correo.setText("")
                    contrasena.setText("")
                }

                else{
                    Toast.makeText(this, "usuario o contraseña incorrectos", Toast.LENGTH_LONG).show()
                }
            }
        }

        else {
            Toast.makeText(this, "Ingresar correo y contraseña", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart(){
        super.onStart()

        val usuarioActual = Firebase.auth.currentUser

        if (usuarioActual!=null){
            startActivity(Intent(this, Home::class.java))
        }
    }
}