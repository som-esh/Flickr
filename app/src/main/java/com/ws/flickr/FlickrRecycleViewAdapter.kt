package com.ws.flickr

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class FlickImageViewHolder(view: View): RecyclerView.ViewHolder(view) {
    var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    var title: TextView = view.findViewById(R.id.title)
}

class FlickrRecycleViewAdapter(private var photoList: List<Photo>): RecyclerView.Adapter<FlickImageViewHolder>() {
    private val tag = "FlickrRecycleViewAdapter"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlickImageViewHolder {
        Log.d(tag,"onCreateViewHolder called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse, parent, false)
        return FlickImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.d(tag, "getItemCount called")
        return if (photoList.isNotEmpty()) photoList.size else 0
    }

    fun loadNewData(newPhotos: List<Photo>) {
        photoList = newPhotos
        notifyDataSetChanged()
    }

    fun getPhoto(position: Int): Photo? {
        return if (photoList.isNotEmpty()) photoList[position] else null
    }

    override fun onBindViewHolder(holder: FlickImageViewHolder, position: Int) {
        val photoItem = photoList[position]
        Log.d(tag, "onBindViewHolder called: ${photoItem.title} --> $position")
        Picasso.with(holder.thumbnail.context).load(photoItem.image)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(holder.thumbnail)
        holder.title.text = photoItem.title
    }
}