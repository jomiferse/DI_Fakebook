package com.example.di_facebook

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fm_comentarios.*
import kotlinx.android.synthetic.main.sn_tbvolver.*
import java.io.IOException
import java.net.URL
import java.util.ArrayList

class addComentarios : AppCompatActivity() {

    val listaComentarios = ArrayList<Comentarios>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fm_comentarios)

        rvComents.setFocusable(false);

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        var likes2 : String? = intent.getStringExtra("likes")
        var comentarios2 : String? = intent.getStringExtra("comentarios")
        var nombre2 : String? = intent.getStringExtra("nombre")
        var tiempo2 : String? = intent.getStringExtra("tiempo")
        var estado2 : String? = intent.getStringExtra("estado")
        var imgperfil : String? = intent.getStringExtra("imgperfil")
        var imgpost : String? = intent.getStringExtra("imgpost")

        Picasso.with(this).load(imgperfil).into(foto_perfil)

        if (imgpost == "") {
            foto_post.visibility = View.GONE
        } else {
            foto_post.visibility = View.VISIBLE
            Picasso.with(this).load(imgpost).into(foto_post)
        }

        likes.setText(likes2)
        comentarios.setText(comentarios2 + " comentarios")
        nombre.setText(nombre2)
        horas.setText(tiempo2)
        estado.setText(estado2)

        ivBackArrow.setOnClickListener(View.OnClickListener {
            finish()
        })

        cargarComentarios()
    }

    fun cargarComentarios() {

        listaComentarios.clear()
        val gson = Gson()
        val recyclerView : RecyclerView
        val adaComentarios : AdapterComentarios

        var id : String? = intent.getStringExtra("id")

        try {
            val json = leerUrl("http://iesayala.ddns.net/jomiferse/json/ConsultaComentariosSQL.php/?id=" + id)
            val comentarios = gson.fromJson(json, ComentariosArray::class.java)

            for (item in comentarios.comentarios!!.iterator()) {
                Log.d("RESULTADO2", Integer.toString(item.idcomentario))
                listaComentarios.add(Comentarios(item.idcomentario,item.id,item.likes,item.imgperfil,item.nombre,item.tiempo,item.estado))
            }

            recyclerView = findViewById(R.id.rvComents) as RecyclerView
            val layoutManager = LinearLayoutManager(this)
            recyclerView.setLayoutManager(layoutManager)
            adaComentarios = AdapterComentarios(this, listaComentarios)
            recyclerView.setAdapter(adaComentarios)

        } catch (e: Exception){
            Log.d("RESULTADO2", "error")
        }
    }

    private fun leerUrl(urlString:String): String{

        val response = try {
            URL(urlString)
                .openStream()
                .bufferedReader()
                .use { it.readText() }
        } catch (e: IOException) {
            "Error with ${e.message}."
        }

        return response
    }
}