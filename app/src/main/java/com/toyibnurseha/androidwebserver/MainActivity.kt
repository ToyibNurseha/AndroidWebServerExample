package com.toyibnurseha.androidwebserver

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.downloader.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val url = "http://gametest.lyto.gilangprambudi.net/SheepFightWithSessionKey.zip"
        val fileName = "game_1"
        val path = File(filesDir, "asset_game").toString()


        btnStart.setOnClickListener {
            val intent = Intent(this, WebServerActivity::class.java)
            startActivity(intent)
        }

        btnNano.setOnClickListener {
            val intent = Intent(this, NanoActivity::class.java)
            startActivity(intent)
        }

        val request = DownloadManager.Request(Uri.parse(url)) as DownloadManager.Request

        request.setTitle("ini judul")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "game_lyto.zip")
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
        btnDownload.setOnClickListener {
            manager.enqueue(request)
        }


        //        embeddedServer(Netty, 8080) {
//            install(ContentNegotiation) {
//                gson {}
//            }
//            routing {
//                get("/") {
//                    call.respond(mapOf("message" to "Hello world"))
//                }
//            }
//        }.start(wait = true)
    }
}