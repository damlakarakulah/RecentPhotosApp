package com.example.recentphotosapp.db

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


class ServiceConfig {

    companion object {
        private const val hasHttpsService = false
        var serviceURL = "https://www.flickr.com/services/rest/?"
        var closedConnection = false
        var httpclient: OkHttpClient? = null
        fun getClient(): OkHttpClient? {
            val okHttpClient: OkHttpClient
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(1, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(1, TimeUnit.MINUTES) // write timeout
                .readTimeout(1, TimeUnit.MINUTES) // read timeout
            okHttpClient = builder.build()
            return okHttpClient
        }
    }
}