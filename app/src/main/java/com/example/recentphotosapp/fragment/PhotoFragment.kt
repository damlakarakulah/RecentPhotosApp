package com.example.recentphotosapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.recentphotosapp.R
import kotlinx.android.synthetic.main.fragment_photo.view.*

class PhotoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_photo, container, false)
        val imageView = view.imageView as ImageView
        val imageUrl = arguments!!.getString("imageUrl")

        Glide.with(context!!).asBitmap().load(imageUrl)
            .error(R.drawable.image_error).into(imageView)
        return view
    }

    companion object {
        fun newInstance(): PhotoFragment {
            return PhotoFragment()
        }
    }

}