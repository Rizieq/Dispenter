package com.waterhub.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.waterhub.R
import com.waterhub.activity.ui.home.HomeScreen
import com.waterhub.data.UserToken
import com.waterhub.networking.Api
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
        fetchUserData()
        goToHome()
    }
    private fun fetchUserData() {
        UserToken.email?.let {
            Api.usersService()
                .profile(it)
                .enqueue(
                    object : retrofit2.Callback<String> {
                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Log.d("LOG_DATA ",t.localizedMessage)
                        }

                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            onUserProfileResultReceived(response)
                        }
                    }
                )
        }
    }

    private fun onUserProfileResultReceived(response: Response<String>) {
        if (response.isSuccessful) {
            val result = response.body().toString()
            if (result != MainHomeActivity.ERROR) {
                val splitResponse = response.body().toString().split(",")

                Log.d("LOG_DATA ",splitResponse[0])
                UserToken.apply {
                    userId = splitResponse[0]
                    userName = splitResponse[1]
                    birthdate = splitResponse[2]
                    gender = splitResponse[4]
                    phone = splitResponse[5]
                }
            } else {
                Toast.makeText(this, "Something wrong, please logout and login again", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun goToHome() {
        val intent = Intent(this, MainHomeActivity::class.java)
        startActivity(intent)
        finish()


    }

    private fun renderPasswordIncorrect(isIncorrect: Boolean) {
        errorMessageLogin.apply {
            text = "wrong password!"
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
