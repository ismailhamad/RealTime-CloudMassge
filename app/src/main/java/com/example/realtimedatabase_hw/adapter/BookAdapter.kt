package com.example.realtimedatabase_hw.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.realtimedatabase_hw.R
import com.example.realtimedatabase_hw.model.Book
import com.example.realtimedatabase_hw.ui.EditBook
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.maxkeppeler.sheets.options.Option
import com.maxkeppeler.sheets.options.OptionsSheet
import kotlinx.android.synthetic.main.custom_item.view.*

class BookAdapter(var activity: Activity, var data: ArrayList<Book>) :
    RecyclerView.Adapter<BookAdapter.viewHolder>() {
    class viewHolder( item: View): RecyclerView.ViewHolder(item){
        var name =item.BookName
        var author=item.BookAuthor
        var year=item.year
        var rate=item.MyRating
        var price=item.price
        var butEdit=item.button
        var image=item.iamgee
        var button_prview =item.button_prview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val root = LayoutInflater.from(activity).inflate(R.layout.custom_item, null)
        return viewHolder(root)
    }


    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.name.text=data[position].BookName
        holder.author.text=data[position].BookAuthor

        holder.year.text=data[position].year.toString().subSequence(0,10)
        holder.price.text="$${data[position].price}"
        holder.rate.rating=data[position].rate
        Glide.with(activity).load(data[position].image).into(holder.image)
        holder.butEdit.setOnClickListener {
            val i = Intent(activity, EditBook::class.java)
            i.putExtra("data", data[position])
            activity.startActivity(i)

        }
        holder.button_prview.setOnClickListener {
            val inflater = activity.layoutInflater
    val inflate_view = inflater.inflate(R.layout.custom_dialog,null)
    val video = inflate_view.findViewById(R.id.videoViewTech) as PlayerView
    val player = ExoPlayer.Builder(activity).build()
    video.player = player
    val mediaItem: MediaItem =
        MediaItem.fromUri(Uri.parse("${data[position].video}"))
    player.setMediaItem(mediaItem)
    player.prepare()
    val alertDialog = AlertDialog.Builder(activity)
    alertDialog.setView(inflate_view)
    val dialog = alertDialog.create()
    dialog.show()
        }


    }

    override fun getItemCount(): Int {
        return data.size
    }


}