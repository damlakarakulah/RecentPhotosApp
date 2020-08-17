package com.firmasoft.infomobil.db

import android.content.Context
import android.os.StrictMode
import android.util.Log
import com.example.recentphotosapp.db.ServiceConfig
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.util.concurrent.TimeUnit

class Connection(
    context: Context?,
    method: String?,
    httpGetURL: String
) {
    private var context: Context? = null
    private var method: String? = null
    private val httpGetURL: String
    private var okHttpClient: OkHttpClient
    private var response: Response? = null
    var params: JSONObject? = null
    var jsonObject: JSONObject? = null

    val connectionMethod: JSONObject?
        get() {
            okHttpClient = getOkHttpClient()
            val policy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            if (method == "POST") {
                try {
                    val body =
                        params.toString().toRequestBody(JSON)
                    val request = Request.Builder()
                        .url(httpGetURL.trim { it <= ' ' })
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .post(body)
                        .build()
                    response = okHttpClient.newCall(request).execute()
                    jsonObject = if (response!!.isSuccessful && response!!.code == 200) {
                        val responseText = response!!.body!!.string()
                        Log.d("Response", responseText)
                        JSONObject(responseText)
                    } else null
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    closeConnection()
                }
            } else if (method == "GET") {
                try {
                    val request = Request.Builder()
                        .url(httpGetURL.trim { it <= ' ' })
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .build()
                    response = getOkHttpClient().newCall(request).execute()
                    jsonObject = if (response!!.isSuccessful && response!!.code == 200) {
                        val responseText = response!!.body!!.string()
                        Log.d("Response", responseText) //
                        JSONObject(responseText)
                    } else null
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    closeConnection()
                }
            } else if (method == "DELETE") {
                try {
                    val body =
                        params.toString().toRequestBody(JSON)
                    val request = Request.Builder()
                        .url(httpGetURL.trim { it <= ' ' })
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .delete(body)
                        .build()
                    response = okHttpClient.newCall(request).execute()
                    if (response!!.isSuccessful && response!!.code == 200) {
                        responseText = response!!.body!!.string()
                        Log.d("Response", responseText)
                        response!!.body!!.close()
                        jsonObject = JSONObject(responseText)
                    } else {
                        jsonObject = null
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    closeConnection()
                }
            } else if (method == "PUT") {
                try {
                    val body =
                        params.toString().toRequestBody(JSON)
                    val request = Request.Builder()
                        .url(httpGetURL.trim { it <= ' ' })
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json")
                        .put(body)
                        .build()
                    response = okHttpClient.newCall(request).execute()
                    if (response!!.isSuccessful && response!!.code == 200) {
                        responseText = response!!.body!!.string()
                        Log.d("Response", responseText)
                        response!!.body!!.close()
                        jsonObject = JSONObject(responseText)
                    } else {
                        jsonObject = null
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    closeConnection()
                }
            }
            return jsonObject
        }

    fun closeConnection() {
        if (response != null && response!!.body != null) response!!.body!!.close()
        if (ServiceConfig.httpclient != null) ServiceConfig.httpclient!!.dispatcher
            .executorService.shutdown()
        response = null
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
            .writeTimeout(5, TimeUnit.MINUTES) // write timeout
            .readTimeout(5, TimeUnit.MINUTES) // read timeout
        okHttpClient = builder.build()
        return okHttpClient
    }

    companion object {
        var responseText = ""
        private val JSON: MediaType? = "application/json; charset=utf-8".toMediaTypeOrNull()
    }

    init {
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        this.context = context
        this.method = method
        this.httpGetURL = httpGetURL
        okHttpClient = ServiceConfig.getClient()!!
        if (ServiceConfig.httpclient == null) {
            ServiceConfig.httpclient = ServiceConfig.getClient()
        }
    }

}
