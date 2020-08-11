package com.toyibnurseha.androidwebserver

import android.os.Environment
import android.util.Log
import fi.iki.elonen.NanoHTTPD
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class NanoServer : NanoHTTPD {
    constructor(port : Int) : super(port) {}
    constructor(hostname : String, port : Int) : super(hostname, port) {}

    override fun serve(
        uri: String?,
        method: Method?,
        headers: MutableMap<String, String>?,
        parms: MutableMap<String, String>?,
        files: MutableMap<String, String>?
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
        return newFixedLengthResponse(Response.Status.OK, "text/html", answer)
    }
}