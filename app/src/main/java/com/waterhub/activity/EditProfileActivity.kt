package com.waterhub.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.waterhub.R
import com.waterhub.data.UserToken
import com.waterhub.networking.Api
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher
import com.waterhub.activity.ui.profile.ProfileScreen
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.fieldBirthDate
import kotlinx.android.synthetic.main.activity_edit_profile.fieldEmail
import kotlinx.android.synthetic.main.activity_edit_profile.fieldFullname
import kotlinx.android.synthetic.main.activity_edit_profile.fieldPhone
import kotlinx.android.synthetic.main.activity_edit_profile.progressBar
import retrofit2.Call
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {

    private var isLoading = false

    private var user = UserToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        renderLoading(false)
        setActionClickListner()
        setUserData()
        setGender()


        user.gender = rdiFemaleProfile.text.toString()
        user.gender = rdiMaleProfile.text.toString()

        fieldBirthDate.addTextChangedListener(PatternedTextWatcher("##/##/####"))

        fieldPhone.addTextChangedListener(PatternedTextWatcher("### ### ### ###"))

    }

    private fun setGender() {


        rdiMaleProfile.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                user.gender = "Male"

            }

        })


        rdiFemaleProfile.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                user.gender = "Female"

            }

        })


    }


    private fun renderLoading(isLoading: Boolean) {
        this.isLoading = isLoading
        progressBar.visibility = View.VISIBLE.takeIf { isLoading } ?: View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun isBirthdateValid(): Boolean {

        var isValid = false
        val birthDate = fieldBirthDate.text.toString().split("/")
        if (birthDate.size == 3) {
            if (birthDate[0].toInt() in 1..30 &&
                birthDate[1].toInt() in 1..12 &&
                birthDate[2].isNotEmpty()
            ) {
                isValid = true
            }
        }
        return isValid
    }

    private fun setActionClickListner() {


        btnEdit.setOnClickListener {
            if (!isLoading && isAllFieldIsComplete()) {

                if (isBirthdateValid()) {
                    updateProfile()
                } else {
                    showWarning("birthdate should using sample format.")
                }

            } else {
                showWarning("Please fill all the field.")
            }
        }

        btnCancel.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }


    private fun updateProfile() {

        renderLoading(true)
        val userData = getUserData()
        Api.usersService()
            .editProfile(userData)
            .enqueue(object : retrofit2.Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    showWarning("Something wrong")
                    renderLoading(false)
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    onUpdateResultReceived(response)
                }
            })
    }

    private fun setUserData() {
        with(UserToken) {
            fieldFullname.setText(userName)
            fieldEmail.setText(email)
            fieldBirthDate.setText(birthdate)
            fieldPhone.setText(phone)

            if (gender == "Male") {
                    rdiMaleProfile?.isChecked = true


            } else if (gender == "Female") {
                rdiFemaleProfile?.isChecked = true

            }

        }
    }

    private fun refreshUserData() {
        Api.usersService()
            .profile(fieldEmail.text.toString())
            .enqueue(object : retrofit2.Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    showWarning("Something wrong")
                    renderLoading(false)
                }

                override fun onResponse(call: Call<String>, response: Response<String>) {
                    onUserProfileResultReceived(response)
                }
            })
    }

    private fun onUserProfileResultReceived(response: Response<String>) {
        if (response.isSuccessful) {
            val result = response.body().toString()
            if (result != ERROR) {
                val splitResponse = response.body().toString().split(",")
                UserToken.apply {
                    userId = splitResponse[0]
                    userName = splitResponse[1]
                    birthdate = splitResponse[2]
                    gender = splitResponse[4]
                    phone = splitResponse[5]

                    Log.d("LOG_USER ", gender)
                }
                updateProfileSuccess()
            } else {
                Toast.makeText(
                    this,
                    "Failed to refresh profile, please logout and login again to refresh your data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        renderLoading(false)
    }

    private fun onUpdateResultReceived(response: Response<String>) {
        if (response.isSuccessful) {
            when (response.body().toString()) {
                UPDATE_SUCCESS -> refreshUserData()
                else -> {
                    showWarning("Something wrong")
                    renderLoading(false)
                }
            }
        } else {
            showWarning("Something wrong")
            renderLoading(false)
        }
    }

    private fun updateProfileSuccess() {
        showWarning("Edit profile success")
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun getUserData(): String {
        val phone = fieldPhone.text.toString()
        val birthdate = fieldBirthDate.text.toString()
        val gender = user.gender
        val fullname = fieldFullname.text.toString()

        return "${UserToken.email},$fullname,$phone,$birthdate,$gender"
    }

    private fun isAllFieldIsComplete(): Boolean {
        val name = fieldFullname.text.isNotBlank()
        val bod = fieldBirthDate.text?.isNotBlank()
        val email = fieldEmail.text.isNotBlank()

        val gender = rdiMaleProfile.isChecked || rdiFemaleProfile.isChecked
        val phone = fieldPhone.text?.isNotBlank()

        return name && bod!! && email && gender && phone!!
    }


    private fun showWarning(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val UPDATE_SUCCESS = "DONE"
        const val ERROR = "ERROR"
    }
}
