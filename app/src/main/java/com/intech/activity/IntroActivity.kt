package com.intech.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.intech.R
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        signButton.setOnClickListener {
            actionGoToRegister()
        }
        loginButton.setOnClickListener {
            actionGoToLogin()
        }
    }

    private fun actionGoToRegister() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun actionGoToLogin() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
