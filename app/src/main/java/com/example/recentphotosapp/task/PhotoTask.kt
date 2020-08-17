package com.example.recentphotosapp.task

import android.content.Context
import android.os.AsyncTask
import com.example.recentphotosapp.db.FetchPhotos

class PhotoTask(var context: Context, asyncResponse: AsyncResponse) :
    AsyncTask<Void, Void, String>() {

    var delegate: AsyncResponse? = null
    private var fetchPhotos: FetchPhotos? = null

    override fun doInBackground(vararg params: Void?): String? {
        fetchPhotos = FetchPhotos(context)
        return ""

    }

    override fun onPostExecute(result: String?) {
        delegate!!.processFinish(result)
    }

    init {
        delegate = asyncResponse
    }
}