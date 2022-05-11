package com.ws.flickr

import android.os.AsyncTask
import android.util.Log
import org.json.JSONObject
import java.lang.Exception

class GetFlickrJsonData(private val listener: OnDataAvailable): AsyncTask<String, Void, ArrayList<Photo>>() {
    interface OnDataAvailable {
        fun onDataAvailable(data: List<Photo>)
        fun onError(exception: Exception)
    }
    private val log: String = "getFlickrJsonData"
    override fun onPostExecute(result: ArrayList<Photo>) {
        Log.d(log, "onPostExecute Starts")
        super.onPostExecute(result)
        listener.onDataAvailable(result)
        Log.d(log,"onPostExecute ends")
    }

    override fun doInBackground(vararg params: String?): ArrayList<Photo> {
        Log.d(log, "doInBackround Starts")
        val photoList = ArrayList<Photo>()
        try {
            val jsonData = JSONObject(params[0])
            val itemsArray = jsonData.getJSONArray("items")

            for (i in 0 until itemsArray.length()) {
                val jsonPhoto = itemsArray.getJSONObject(i)
                val title = jsonPhoto.getString("title")
                val author = jsonPhoto.getString("author")
                val authorId = jsonPhoto.getString("author_id")
                val tags = jsonPhoto.getString("tags")

                val jsonMedia = jsonPhoto.getJSONObject("media")
                val photoUrl = jsonMedia.getString("m")
                val link = photoUrl.replaceFirst("_m.jpg","_b.jpg")

                val photoObject = Photo(title, author, authorId, link, tags, photoUrl)
                photoList.add(photoObject)
                Log.d(log, "doInBackground $photoObject called")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(log,"Problems ${e.message}")
            cancel(true)
            listener.onError(e)
        }
        Log.d(log,"doInBackground ends")
        return photoList
    }
}