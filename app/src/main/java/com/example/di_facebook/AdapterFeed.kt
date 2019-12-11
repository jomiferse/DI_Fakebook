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


class AdapterFeed(var mContext: Context, var listaF: ArrayList<Feed>): RecyclerView.Adapter<AdapterFeed.MyViewHolder>(){

    var cont = 0
    var likes = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.row_feed, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, posicion: Int) {
        holder.tv_name.setText(listaF[posicion].nombre)
        holder.tv_time.setText(listaF[posicion].tiempo)
        holder.tv_likes.setText(Integer.toString(listaF[posicion].likes))
        holder.tv_comments.setText(Integer.toString(listaF[posicion].comentarios) + " comentarios")
        holder.tv_status.setText(listaF[posicion].estado)
        holder.rel_bt1.setOnClickListener(View.OnClickListener {

            if (cont == 0) {
                val url = "http://iesayala.ddns.net/jomiferse/json/incrementarLikes.php/?id=" + listaF[posicion].id  + "&likes=" + listaF[posicion].likes
                leerUrl(url)
                holder.tv_likes.setText(Integer.toString(listaF[posicion].likes + 1))
                holder.bt_likes.setImageResource(R.drawable.like)
                cont = 1
                likes = 1
            } else if (cont == 1) {
                val url = "http://iesayala.ddns.net/jomiferse/json/decrementarLikes.php/?id=" + listaF[posicion].id  + "&likes=" + (listaF[posicion].likes + 1)
                leerUrl(url)
                holder.tv_likes.setText(Integer.toString(listaF[posicion].likes))
                holder.bt_likes.setImageResource(R.drawable.ic_like_btn)
                cont = 0
                likes = 0
            }
        })

        holder.rel_bt2.setOnClickListener(View.OnClickListener {
            val i = Intent(mContext, addComentarios::class.java)
            i.putExtra("id",Integer.toString(listaF[posicion].id))
            Log.d("RESULTADOID", Integer.toString(listaF[posicion].id))
            if (likes == 1) {
                i.putExtra("likes",Integer.toString(listaF[posicion].likes + 1))
            } else if (likes == 0) {
                i.putExtra("likes",Integer.toString(listaF[posicion].likes))
            }
            i.putExtra("comentarios",Integer.toString(listaF[posicion].comentarios))
            i.putExtra("imgperfil",listaF[posicion].imgperfil)
            i.putExtra("imgpost",listaF[posicion].imgpost)
            i.putExtra("nombre",listaF[posicion].nombre)
            i.putExtra("tiempo",listaF[posicion].tiempo)
            i.putExtra("estado",listaF[posicion].estado)
            mContext.startActivity(i)
        })

        holder.rel_bt3.setOnClickListener(View.OnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, listaF[posicion].estado);
            mContext.startActivity(Intent.createChooser(shareIntent,"Compartir"))
        })

        Picasso.with(mContext).load(listaF[posicion].imgperfil).into(holder.imgView_proPic)

        if (listaF[posicion].imgpost == "") {
            holder.imgView_postPic.visibility = View.GONE
        } else {
            holder.imgView_postPic.visibility = View.VISIBLE
            Picasso.with(mContext).load(listaF[posicion].imgpost).into(holder.imgView_postPic)
        }
    }

    override fun getItemCount(): Int {
        return listaF.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var tv_name: TextView
        internal var tv_time: TextView
        internal var tv_likes: TextView
        internal var tv_comments: TextView
        internal var tv_status: TextView
        internal var imgView_proPic: ImageView
        internal var imgView_postPic: ImageView
        internal var rel_bt1 : RelativeLayout
        internal var rel_bt2 : RelativeLayout
        internal var rel_bt3 : RelativeLayout
        internal var bt_likes : ImageView

        init {

            imgView_proPic = itemView.findViewById(R.id.imgView_proPic) as ImageView
            imgView_postPic = itemView.findViewById(R.id.imgView_postPic) as ImageView

            tv_name = itemView.findViewById(R.id.tv_name) as TextView
            tv_time = itemView.findViewById(R.id.tv_time) as TextView
            tv_likes = itemView.findViewById(R.id.tv_like) as TextView
            tv_comments = itemView.findViewById(R.id.tv_comment) as TextView
            tv_status = itemView.findViewById(R.id.tv_status) as TextView
            rel_bt1 = itemView.findViewById(R.id.relbt1) as RelativeLayout
            rel_bt2 = itemView.findViewById(R.id.relbt2) as RelativeLayout
            rel_bt3 = itemView.findViewById(R.id.relbt3) as RelativeLayout
            bt_likes = itemView.findViewById(R.id.bt_like) as ImageView
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