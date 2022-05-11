package com.ws.flickr

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.lang.Exception
import java.net.MalformedURLException
import java.net.URL

enum class DownloadStatus{
    OK, IDLE, NOT_INITIALIZED, FAILED_OR_EMPTY, PERMISSION_ERROR, ERROR
}

class GetRawData(private val listener: OnDownloadComplete): AsyncTask<String, Void, String>() {
    private val log = "GetRawData"
    private var downloadStatus = DownloadStatus.IDLE

    interface OnDownloadComplete {
        fun onDownloadComplete(data: String, status: DownloadStatus)
    }
//    private var listener: MainActivity? = null
//
//    fun setDownloadCompleteListener(callbackObject: MainActivity) {
//        listener = callbackObject
//    }

    override fun onPostExecute(result: String) {
        Log.d(log,"onPostExecute Called:")
        listener?.onDownloadComplete(result, downloadStatus)
    }

    override fun doInBackground(vararg params: String?): String {
        if (params[0] == null) {
            downloadStatus = DownloadStatus.NOT_INITIALIZED
            return "NO URL SPECIFIED"
        }
        try {
            downloadStatus = DownloadStatus.OK
            return URL(params[0]).readText()
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALIZED
                    "doInBackground: Invalid URl ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground: IO Exception Cannot Write ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALIZED
                    "doInBackground: Security Exception Missing Permission ${e.message}"
                }
                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "doInBackground: Unknown Exception ${e.message}"
                }
            }
            Log.e(log, errorMessage)
            return errorMessage
        }
    }
}