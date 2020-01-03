package com.waterhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.waterhub.R
class TumblerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tumbler)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
