package com.waterhub.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.waterhub.R
import kotlinx.android.synthetic.main.activity_about_us.*


class AboutUsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        webAboutUs.webViewClient = WebViewClient()
        webAboutUs.settings.javaScriptEnabled = true
        webAboutUs.loadUrl("https://waterhub.id")



    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webAboutUs.canGoBack()) {
            webAboutUs.goBack()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
