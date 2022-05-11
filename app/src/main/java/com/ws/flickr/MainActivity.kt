package com.ws.flickr

import android.location.Criteria
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), GetRawData.OnDownloadComplete, GetFlickrJsonData.OnDataAvailable {

    private val flickrRecycleViewAdapter = FlickrRecycleViewAdapter(ArrayList())
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(log,"OnCreate called")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = flickrRecycleViewAdapter
        val url = createUri("https://www.flickr.com/services/feeds/photos_public.gne", "sunset, oreo", "en-us", true)
        val getRawData = GetRawData(this)
        getRawData.execute(url)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        Log.d(log,"OnCreate ends")
    }

    private fun createUri(baseUrl: String, searchCriteria: String, lang: String, matchAll: Boolean): String {
        Log.d(log, "createUri called:")
        return Uri.parse(baseUrl)
            .buildUpon()
            .appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", if(matchAll) "ALL" else "ANY")
            .appendQueryParameter("lang", lang)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .build()
            .toString()
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        Log.d(log,"OnCreateOptionsMenu called")
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        Log.d(log,"OnCreateOptionsMenu ends")
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(log,"OnOptionsItemsSelected called")
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
    companion object {
        private const val log = "MainActivity"
    }
    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(log,"onDownloadComplete called:")
            val getFlickrJsonData = GetFlickrJsonData(this)
            getFlickrJsonData.execute(data)
        } else {
            Log.d(log, "onDownloadComplete failed. $status. Error is $data")
        }
    }

    override fun onDataAvailable(data: List<Photo>) {
        Log.d(log,"onDataAvailable called:")
        flickrRecycleViewAdapter.loadNewData(data)
        Log.d(log,"onDataAvailable ends")
    }

    override fun onError(exception: Exception) {
        Log.e(log,"${exception.message}")
    }
}
