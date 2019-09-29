package com.intech.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.intech.R
import com.intech.networking.Api
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    // state of view
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setActionClickListner()
        renderLoading(false)
    }

    fun renderLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        progressBar.visibility = View.VISIBLE.takeIf { isLoading } ?: View.GONE
    }

    private fun setActionClickListner() {
        signButton.setOnClickListener {
            if (!isLoading) {
                doRegister()
            }
        }
    }

    private fun doRegister() {
        if (isAllFieldIsComplete()) {
            if (isBirthdateValid()) {
                renderLoading(true)
                val userData = getDataUser()
                Api.usersService()
                    .register(userData)
                    .enqueue(
                        object : Callback<String> {
                            override fun onFailure(call: Call<String>, t: Throwable) {
                                showErrorNetwork()
                                renderLoading(false)
                            }

                            override fun onResponse(call: Call<String>, response: Response<String>) {
                                onRegistrationResultReceived(response)
                                renderLoading(false)
                            }
                        }
                    )
            } else {
                showToast("birthdate should using sample format.")
            }
        } else {
            showToast("Please fill all the field.")
        }
    }

    private fun isAllFieldIsComplete(): Boolean {
        val name = fieldFullname.text.isNotBlank()
        val bod = fieldBirthDate.text.isNotBlank()
        val email = fieldEmail.text.isNotBlank()
        val gender = fieldGender.text.isNotBlank()
        val phone = fieldPhone.text.isNotBlank()
        val password = fieldPassword.text.isNotBlank()
        return name && bod && email && gender && phone && password
    }

    private fun isBirthdateValid(): Boolean {
        var isValid = false
        val birthDate = fieldBirthDate.text.toString().split("/")
        if (birthDate.size == 3) {
            if (birthDate[0].toInt() in 1..30 &&
                birthDate[1].toInt() in 1..12) {
                isValid = true
            }
        }
        return isValid
    }

    private fun getDataUser(): String {
        val name = fieldFullname.text
        val bod = fieldBirthDate.text
        val email = fieldEmail.text
        val gender = fieldGender.text
        val phone = fieldPhone.text
        val password = fieldPassword.text
        return "$name,$bod,$email,$gender,$phone,$password"
    }

    private fun onRegistrationResultReceived(response: Response<String>) {
        if (response.isSuccessful) {
            when (response.body().toString()) {
                EXIST -> emailAlreadyTaken()
                SUCCESS -> registrationSuccess()
            }
        } else {
            showErrorNetwork()
        }
    }

    private fun registrationSuccess() {
        showToast("Registration complete, please login.")
        finish()
    }

    private fun emailAlreadyTaken() {
        showToast("Registration failed, email already registered")
    }

    private fun showErrorNetwork() {
        showToast("Error network, please check your connection")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXIST = "EXIST"
        const val SUCCESS = "DONE"
    }
}
