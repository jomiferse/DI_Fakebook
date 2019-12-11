package com.example.di_facebook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import java.io.IOException
import java.net.URL
import java.util.ArrayList
import android.content.Intent
import android.util.Log


class AdapterComentarios(var mContext: Context, var listaC: ArrayList<Comentarios>): RecyclerView.Adapter<AdapterComentarios.MyViewHolder>(){

    var cont = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_coments, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, posicion: Int) {
        holder.tv_name_co.setText(listaC[posicion].nombre)
        holder.tv_time_co.setText(listaC[posicion].tiempo)
        holder.tv_likes_co.setText(Integer.toString(listaC[posicion].likes))
        holder.tv_status_co.setText(listaC[posicion].estado)
        holder.rel_bt1_co.setOnClickListener(View.OnClickListener {

            if (cont == 0) {
                val url = "http://iesayala.ddns.net/jomiferse/json/incrementarGusta.php/?idcomentario=" + listaC[posicion].idcomentario  + "&likes=" + listaC[posicion].likes
                leerUrl(url)
                holder.tv_likes_co.setText(Integer.toString(listaC[posicion].likes + 1))
                holder.bt_likes_co.setImageResource(R.drawable.corazoncolor)
                cont = 1
            } else if (cont == 1) {
                val url = "http://iesayala.ddns.net/jomiferse/json/decrementarGusta.php/?idcomentario=" + listaC[posicion].idcomentario  + "&likes=" + (listaC[posicion].likes + 1)
                leerUrl(url)
                holder.tv_likes_co.setText(Integer.toString(listaC[posicion].likes))
                holder.bt_likes_co.setImageResource(R.drawable.corazonvacio)
                cont = 0
            }
        })

        holder.rel_bt2_co.setOnClickListener(View.OnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, listaC[posicion].estado);
            mContext.startActivity(Intent.createChooser(shareIntent,"Compartir"))
        })

        Picasso.with(mContext).load(listaC[posicion].imgperfil).into(holder.imgView_proPic_co)
    }

    override fun getItemCount(): Int {
        return listaC.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var tv_name_co: TextView
        internal var tv_time_co: TextView
        internal var tv_status_co: TextView
        internal var tv_likes_co: TextView
        internal var imgView_proPic_co: ImageView
        internal var rel_bt1_co : RelativeLayout
        internal var rel_bt2_co : RelativeLayout
        internal var bt_likes_co : ImageView

        init {

            imgView_proPic_co = itemView.findViewById(R.id.imgView_proPic_co) as ImageView

            tv_name_co = itemView.findViewById(R.id.tv_name_co) as TextView
            tv_time_co = itemView.findViewById(R.id.tv_time_co) as TextView
            tv_status_co = itemView.findViewById(R.id.tv_status_co) as TextView
            tv_likes_co = itemView.findViewById(R.id.tv_like_co) as TextView
            rel_bt1_co = itemView.findViewById(R.id.relbt1_co) as RelativeLayout
            rel_bt2_co = itemView.findViewById(R.id.relbt2_co) as RelativeLayout
            bt_likes_co = itemView.findViewById(R.id.bt_like_co) as ImageView
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