package com.intech.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.intech.R
import com.intech.data.UserToken
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        checkUserLoginOrNotLogin()

        signButton.setOnClickListener {
            actionGoToRegister()
        }
        loginButton.setOnClickListener {
            actionGoToLogin()
        }
    }

    /**
     * will bypass user to home screen
     * when user is already login
     * and have session on this app.
     */
    private fun checkUserLoginOrNotLogin() {
        if (!UserToken.email.isNullOrEmpty()) {
            forceToHome()
        }
    }

    private fun forceToHome() {
        startActivity(Intent(this, MainHomeActivity::class.java))
        finish()
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
