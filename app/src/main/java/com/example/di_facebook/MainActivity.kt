package com.example.di_facebook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.row_feed.*
import java.io.IOException
import java.net.URL
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    val listaFeed = ArrayList<Feed>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        cargarFeed()
    }

    fun cargarFeed() {

        listaFeed.clear()
        val gson = Gson()
        val recyclerView : RecyclerView
        val adapterFeed : AdapterFeed

        try {
            val json = leerUrl("http://iesayala.ddns.net/jomiferse/json/ConsultaFeedSQL.php")
            val feed = gson.fromJson(json, FeedArray::class.java)

            for (item in feed.feed!!.iterator()) {
                Log.d("RESULTADO", item.nombre)
                listaFeed.add(Feed(item.id,item.likes,item.comentarios,item.imgperfil,item.imgpost,item.nombre,item.tiempo,item.estado))
            }

            recyclerView = findViewById(R.id.recyclerView) as RecyclerView
            val layoutManager = LinearLayoutManager(this)
            recyclerView.setLayoutManager(layoutManager)
            adapterFeed = AdapterFeed(this, listaFeed)
            recyclerView.setAdapter(adapterFeed)

        } catch (e: Exception){
            Log.d("RESULTADO", "error")
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
