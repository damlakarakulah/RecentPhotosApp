package com.example.recentphotosapp.db

import android.content.Context
import com.example.recentphotosapp.model.Photo
import com.example.recentphotosapp.util.Config
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class FetchPhotos(private val context: Context) {
    private var photoList: MutableList<Photo>? = null
    private var callService: CallService? = null
    private var url: String
    private var photoArray: JSONArray? = null
    private var photoObject: JSONObject? = null
    private val photos: Unit
        private get() {
            val params = JSONObject()
            try {
                callService = CallService(context)
                url =
                    ServiceConfig.serviceURL + "method=flickr.photos.getRecent&api_key=2b596690015697bfec463dc2923b0e7f&per_page=" + per_page + "&page=" + page + "&format=json&nojsoncallback=1"
                val jsonObject = callService!!.getService(params, "GET", url) ?: return
                if (jsonObject.has("stat")) {
                    if (!jsonObject.getString("stat").equals("ok"))
                        return
                    else
                        page++
                }
                if (jsonObject.has("photos")) {
                    photoObject = jsonObject.getJSONObject("photos")
                    photoArray = getPhotoArray(photoObject)
                    photoList = createPhotoList(photoArray)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

    private fun getPhotoArray(photo: JSONObject?): JSONArray {
        var photoArray: JSONArray = JSONArray()
        if (photo!!.has("photo") && photo.getJSONArray("photo") != null) {
            photoArray = photo.getJSONArray("photo")
        }
        return photoArray
    }

    private fun createPhotoList(photos: JSONArray?): MutableList<Photo> {
        val photoList: MutableList<Photo> = ArrayList<Photo>()
        for (i in 0 until photos!!.length()) {
            val jsonObject = photos.optJSONObject(i)
            val jsonString = jsonObject.toString()
            val gson = Gson()
            val photo: Photo = gson.fromJson(jsonString, Photo::class.java)
            photoList.add(photo)
        }
        Config.photos = photoList
        return photoList
    }

    init {
        url = ServiceConfig.serviceURL
        photos
    }

    companion object {
        var per_page = 20
        var page = 1
    }
}