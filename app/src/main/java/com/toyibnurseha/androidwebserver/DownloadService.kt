package com.toyibnurseha.androidwebserver

import android.app.DownloadManager
import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.net.Uri


class DownloadService : IntentService("DownloadService") {

    companion object {
        const val DOWNLOAD_PATH = "com.toyibnurseha.androidwebserver_DownloadService_Download_path"
        const val DESTINATION_PATH = "com.toyibnurseha.androidwebserver_DownloadService_Destination_path"
    }

    fun getDownloadService(
        callingClassContext: Context,
        downloadPath: String,
        destinationPath: String
    ): Intent? {
        return Intent(callingClassContext, DownloadService::class.java)
            .putExtra(DOWNLOAD_PATH, downloadPath)
            .putExtra(DESTINATION_PATH, destinationPath)
    }


    override fun onHandleIntent(intent: Intent?) {
        val downloadPath = intent?.getStringExtra(DOWNLOAD_PATH)
        val destinationPath = intent?.getStringExtra(DESTINATION_PATH)
        startDownload(downloadPath, destinationPath)
    }

    private fun startDownload(downloadPath: String?, destinationPath: String?) {
        val uri = Uri.parse(downloadPath)
        val request = DownloadManager.Request(uri) as DownloadManager.Request
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setTitle("downloading game")
        request.setVisibleInDownloadsUi(true)
        request.setDestinationInExternalPublicDir(destinationPath, uri.lastPathSegment) //storage
        (getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).enqueue(request)
    }
}