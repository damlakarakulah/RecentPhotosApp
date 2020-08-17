package com.example.recentphotosapp.db

import android.content.Context
import com.firmasoft.infomobil.db.Connection
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class CallService(private val context: Context?) {
    private var connection: Connection? = null
    private var returnObject: JSONObject? = null
    private val returnArray: JSONArray? = null
    private var method: String? = null
    private var httpGetURL: String? = null
    private val response: Response? = null
    fun createConnection() {
        if (method != null) connection =
            Connection(context, method, httpGetURL!!)
    }

    @Throws(JSONException::class)
    fun getService(
        params: JSONObject?,
        method: String?,
        httpGetURL: String?
    ): JSONObject? {
        if (context != null) {
            this.method = method
            this.httpGetURL = httpGetURL
            if (connection != null && !ServiceConfig.closedConnection) {
                connection!!.closeConnection()
                while (ServiceConfig.closedConnection) {
                    connection = null
                    createConnection()
                    connection!!.params = params
                    returnObject = connection!!.connectionMethod
                    break
                }
            } else {
                connection = null
                createConnection()
                connection!!.params = params
                returnObject = connection!!.connectionMethod
            }
        }
        return if (returnObject != null) returnObject else {
            null
        }
    }

}