package com.waterhub.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import com.waterhub.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        imageView2.visibility = View.VISIBLE

        delay()

        Handler().postDelayed({
            startActivity(Intent(this,IntroActivity::class.java))
            finish()
        },4000)



    }


    override fun onRestart() {
        super.onRestart()
    }

    private fun delay(){

        Handler().postDelayed({
            imageView3.visibility = View.VISIBLE

            Log.d("LOG_D","Handler_1")

        },1000)
        Handler().postDelayed({
            imageView2.visibility = View.INVISIBLE
            Log.d("LOG_D","Handler_1")

        },1000)
        Handler().postDelayed({
            imageView2.visibility = View.VISIBLE
            Log.d("LOG_D","Handler_1")

        },2000)

        Handler().postDelayed({
            imageView3.visibility = View.INVISIBLE
            Log.d("LOG_D","Handler_2")
        },2000)




    }




}