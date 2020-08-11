package com.toyibnurseha.androidwebserver

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import fi.iki.elonen.NanoHTTPD
import kotlinx.android.synthetic.main.activity_nano.*
import kotlinx.io.errors.IOException
import java.io.BufferedReader
import java.io.FileReader

class NanoActivity : AppCompatActivity() {

    private lateinit var server : WebServer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nano)


        val nano = NanoServer(8080)
        nano.start()

        getIp()

        portTv.text = getIp()
    }

    private fun getIp() : String {
        val wifiManager  = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val ipAddress = wifiManager.connectionInfo.ipAddress
        val formatedIpAddress = String.format(
            "%d.%d.%d.%d",
            ipAddress and 0xff,
            ipAddress shr 8 and 0xff,
            ipAddress shr 16 and 0xff,
            ipAddress shr 24 and 0xff
        )

        return "http://$formatedIpAddress:"
    }

    override fun onDestroy() {
        server = WebServer()
        server.stop()
        super.onDestroy()
    }

    private class WebServer : NanoHTTPD(8080) {
        override fun serve(
            uri: String, method: Method,
            header: Map<String, String>,
            parameters: Map<String, String>,
            files: Map<String, String>
        ): Response {
            var answer: String? = ""
            try {
                // Open file from SD Card
                val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                Log.d("ROOT", "serve: $root")
                val index = FileReader (
                    "$root/index.html"
                )
                val reader = BufferedReader(index)
                var line: String? = ""
                while (reader.readLine().also { line = it } != null) {
                    answer += line
                }
                reader.close()
            } catch (ioe: IOException) {
                Log.w("Httpd", ioe.toString())
            }
            return newFixedLengthResponse(answer)
        }
    }
}