package com.intech.activity

import android.content.Intent
import android.net.wifi.hotspot2.pps.HomeSp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.intech.R
import com.intech.activity.ui.home.HomeScreen
import com.intech.data.UserToken
import com.intech.networking.Api
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    // state of view
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setActionClickListener()
        renderLoading(false)
        renderPasswordIncorrect(false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESET_PASSWORD) {
            finish()
        }
    }

    private fun setActionClickListener() {
        signButton.setOnClickListener {
            if (!isLoading) {
                doLogin()
            }
        }
        forgotPassword.setOnClickListener {
            goToResetPasswordScreen()
        }
    }

    private fun goToResetPasswordScreen() {
        val intent = Intent(this, ResetPasswordActivity::class.java)
        startActivityForResult(intent, RESET_PASSWORD)
    }

    private fun renderLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        progressBar.visibility = View.VISIBLE.takeIf { isLoading } ?: View.GONE
    }

    private fun doLogin() {
        renderLoading(true)
        renderPasswordIncorrect(false)

        Api.usersService()
            .login(getUserData())
            .enqueue(
                object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        showToast("Something error")
                        renderLoading(false)
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        onLoginResultReceived(response)
                    }
                }
            )
    }

    private fun onLoginResultReceived(response: Response<String>) {
        if (response.isSuccessful) {
            when (response.body().toString()) {
                LOGIN_SUCCESS -> loginComplete()
                LOGIN_FAILED -> renderPasswordIncorrect(true)
            }
        } else {
            showToast("Something error")
        }
        renderLoading(false)
    }

    private fun loginComplete() {
        UserToken.email = fieldEmail.text.toString()
        showToast("login success. Welcome ${UserToken.email}")
        goToHome()
    }

    private fun goToHome() {
        val intent = Intent(this, MainHomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun renderPasswordIncorrect(isIncorrect: Boolean) {
        errorMessageLogin.apply {
            text = "Password salah!"
            visibility = View.VISIBLE.takeIf { isIncorrect } ?: View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getUserData(): String {
        val email = fieldEmail.text
        val password = fieldPassword.text
        return "$email,$password"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val LOGIN_SUCCESS = "VALID"
        const val LOGIN_FAILED = "ERROR"
        const val RESET_PASSWORD = 200
    }
}
