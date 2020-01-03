package com.waterhub.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.waterhub.R
import com.waterhub.networking.Api
import kotlinx.android.synthetic.main.activity_reset_password.*
import retrofit2.Call
import retrofit2.Response

class ResetPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setActionClickListner()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setActionClickListner() {
        resetPasswordButton.setOnClickListener {
            resetPassword()
        }
    }

    private fun resetPassword() {
        val email = fieldEmail.text.toString()

        Api.usersService()
            .resetPassword(email)
            .enqueue(object : retrofit2.Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    showErrorMessage("Something error, please check your connection")
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    onResetResultReceived(response)


                }
            })
    }

    private fun onResetResultReceived(response: Response<String>) {
        if (response.isSuccessful) {
            Log.d("RESPONSE_RESET ",response.body().toString())
            showErrorMessage("Reset password success, please check your email")
            setResult(LoginActivity.RESET_PASSWORD)
            finish()
        } else {
            showErrorMessage("Something error, please check your connection")
        }
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val RESET_PASSWORD_SUCCESS = "OK"
    }
}
