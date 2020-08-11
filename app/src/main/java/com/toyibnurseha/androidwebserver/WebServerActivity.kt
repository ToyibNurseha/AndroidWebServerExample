package com.toyibnurseha.androidwebserver

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.view.View
import android.view.WindowManager
import android.webkit.WebSettings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_web_server.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

class WebServerActivity : AppCompatActivity() {

    private lateinit var httpClient: OkHttpClient
    private lateinit var viewModel: WebViewModel

    @SuppressLint("SetJavaScriptEnabled", "InvalidWakeLockTag", "WakelockTimeout")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_server)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        showSystemUI()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(WebViewModel::class.java)
        viewModel.startSocket()
        viewModel.getToken().observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            webViewLoad(it)
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewLoad(gameToken: String){

        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.domStorageEnabled = true
        webView.settings.setAppCacheEnabled(true)
        webView.settings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        val getPath = filesDir.path + "/cache"
        webView.settings.setAppCachePath(getPath)
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        val pathInternal = "http://gametest2.lyto.gilangprambudi.net/"

        webView.loadUrl("$pathInternal/?user-game-token=$gameToken")
    }

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                //set the content to appear under system bars
                //content doesnt resize when system bars hide and show
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                //status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}