package com.example.recentphotosapp.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recentphotosapp.R
import com.example.recentphotosapp.activity.MainActivity
import com.example.recentphotosapp.fragment.PhotoFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_grid.view.*


class ImageGridAdapter(
    private val context: Context,
    private val images: ArrayList<String>
) :
    RecyclerView.Adapter<ImageGridAdapter.PhotoViewHolder>() {

    override fun getItemCount(): Int {
        return images.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_grid, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val path = images[position]

        Glide.with(context).asBitmap().load(path).centerCrop()
            .error(R.drawable.image_error).into(holder.imageView)

        val bundle = Bundle()
        bundle.putInt("imageId", position)
        bundle.putString("imageUrl", path)

        holder.imageView.setOnClickListener {
            MainActivity.Instance!!.changeFragment(
                PhotoFragment.newInstance(),
                "Photo", "PhotoFragment", bundle
            )
            MainActivity.Instance!!.viewLayout.visibility = View.VISIBLE
        }

        end = position == images.size - 1

    }

    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView = view.iv as ImageView
    }

    companion object {
        var end = false
    }
}