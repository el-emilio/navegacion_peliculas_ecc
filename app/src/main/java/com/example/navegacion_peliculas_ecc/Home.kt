package com.example.navegacion_peliculas_ecc

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.Toast
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
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
            val pelicula = pelicula("nombre", "genero", "anio")
            myRef.push().setValue(pelicula).addOnCompleteListener {
                task ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Pelicula agregada", Toast.LENGTH_LONG).show()
                }
            }
        }

        val listView = findViewById<ListView>(R.id.lista)

        listView.setOnItemClickListener{
            parent, view, position, id ->

            Toast.makeText(this, peliculas[position].nombre.toString(), Toast.LENGTH_LONG).show()
        }

        /*
        val logout = findViewById<Button>(R.id.logout)
        val saludo = findViewById<TextView>(R.id.saludo)

        saludo.text = saludo.text.toString()+extras?.getCharSequence("email").toString()

        logout.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
 */

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