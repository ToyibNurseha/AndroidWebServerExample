package com.toyibnurseha.androidwebserver

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.WebSocket
import okio.ByteString
import org.json.JSONObject

open class WebSocket : WebSocketListener() {

    private lateinit var responseScore : ArrayList<ResponseModel>
    private lateinit var httpClient: OkHttpClient

    private val _liveData = MutableLiveData<String>()
    private val liveData: LiveData<String> get() = _liveData

    override fun onOpen(webSocket: WebSocket?, response: Response?) {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("user_token", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOjIsInJvbGUiOiJ1c2VyIiwiZXhwIjoxNTk4NjcxODQyfQ.rvLoIef2byRDBzQSIu7oW1Q-zJsEluS3oMyV72XM3rw")
        }catch (e: Exception){
            Log.d(TAG, "onError: ${e.message}")
        }

        webSocket?.send("/join $jsonObject")
        super.onOpen(webSocket, response)
    }

    override fun onMessage(webSocket: WebSocket?, text: String?) {
        responseScore = ArrayList()
//            val url = text
        val response = text!!
        val indicator = response.substring(0, response.indexOf(" "))

        if (indicator == "/join") {
            val url = text?.replaceFirst("/join", "")
            val obj = JSONObject(url!!)
            val gameToken = obj.getString("game_token")
            Log.d(TAG, "onMessage: $gameToken")
            outputData(gameToken)
        } else if (indicator == "/room-session-finish") {
            val url = text?.replaceFirst("/room-session-finish", "")
            val obj = JSONObject(url)
            val playerScores = obj.getJSONArray("player_score_summaries")
            for (i in 0 until playerScores.length()) {
                val item = playerScores.getJSONObject(i)
                val score = item.getInt("score")
                val winStatus = item.getInt("win_status")
                val userId = item.getInt("user_id")
                Log.d(TAG, "onMessage: $score")
                Log.d(TAG, "onMessage: $winStatus")
                Log.d(TAG, "onMessage: $userId")
                val output = "$score, $winStatus, $userId"
            }

            super.onMessage(webSocket, text)
        }
    }


    override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
        Log.d("Receive log : ", bytes?.hex())
        super.onMessage(webSocket, bytes)
    }

    fun outputData(string: String) : String {
        return string
    }

    fun startListen(): LiveData<String> {
        val listener = WebSocket()
        GlobalScope.launch(Dispatchers.IO) {
            httpClient = OkHttpClient()
            val request = Request.Builder()
                .url("ws://lytodev.space:5009/api/ws/game-session-rooms")
                .build()
            val webSocket = httpClient.newWebSocket(request, listener)
            httpClient.dispatcher().executorService().shutdown()
        }
        return _liveData
    }

}