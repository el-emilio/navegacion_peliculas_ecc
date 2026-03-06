package com.example.navegacion_peliculas_ecc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

//import com.google.firebase.ktx.Firebase


class Home : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    val database = Firebase.database
    val myRef = database.getReference("peliculas")

    lateinit var peliculas: ArrayList<Peliculas>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.eliminar)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val extras= intent.extras
        auth = com.google.firebase.Firebase.auth

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val agregarPeliculas = findViewById<FloatingActionButton>(R.id.agregoPelicula)

        agregarPeliculas.setOnClickListener {
            startActivity(Intent(this, agregar::class.java))

        }

        val listView = findViewById<ListView>(R.id.lista)

        listView.setOnItemClickListener{
            parent, view, position, id ->

            //Toast.makeText(this, peliculas[position].nombre.toString(), Toast.LENGTH_LONG).show()

            val seleccion = peliculas[position]

            val intent = Intent(this, detalle::class.java).apply {
                putExtra("nombre", seleccion.nombre)
                putExtra("genero", seleccion.genero)
                putExtra("anio", seleccion.anio)
                putExtra("id", seleccion.id)
            }
            startActivity(intent)
        }


        // Read from the database
        myRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                peliculas = ArrayList<Peliculas>()

                val value = snapshot.value
                Log.d("real-time-database", "Value is: " + value)
                snapshot.children.forEach{

                    unit ->
                    var pelicula = Peliculas(unit.child("nombre").value.toString(),
                        peliAnio = unit.child("anio").value.toString(),
                        peliGenero = unit.child("genero").value.toString(),
                        peliId = unit.key.toString())
                    peliculas.add(pelicula)
                }
                llenarListView()
            }


            override fun onCancelled(error: DatabaseError) {
                Log.w("real-time-database", "Failed to read value.", error.toException())
            }

        })

    }

    fun llenarListView(){
        val lista = findViewById<ListView>(R.id.lista)
        val adaptador = PeliAdapter(this, peliculas)
        lista.adapter = adaptador
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout){
            auth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}