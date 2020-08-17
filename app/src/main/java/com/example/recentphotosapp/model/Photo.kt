package com.example.recentphotosapp.model

class Photo {
    fun getUrl(): String {
        return "https://farm" + farm + ".staticflickr.com/" + server + "/" + id + "_" + secret + ".jpg"
    }

    var id: String? = null
    var owner: String? = null
    var secret: String? = null
    var server: String? = null
    var farm = 0
    var title: String? = null
    var ispublic = 0
    var isfriend = 0
    var isfamily = 0

}