package com.intech.data

import android.content.Context
import com.intech.DispenterApp

object UserToken {
    private const val SESSION_USER = "session"
    private const val USER_ID = "userID"
    private const val USER_NAME = "userName"
    private const val EMAIL = "email"
    private const val BIRTHDATE = "birthdate"
    private const val GENDER = "gender"
    private const val PHONE = "phone"

    var userId: String?
    get() {
        return getValue(USER_ID)
    }
    set(value) {
        commitValue(USER_ID, value)
    }
    var userName: String?
    get() {
        return getValue(USER_NAME)
    }
    set(value) {
        commitValue(USER_NAME, value)
    }
    var email: String?
    get() {
        return getValue(EMAIL)
    }
    set(value) {
        commitValue(EMAIL, value)
    }
    var birthdate: String?
    get() {
        return getValue(BIRTHDATE)
    }
    set(value) {
        commitValue(BIRTHDATE, value)
    }
    var gender: String?
    get() {
        return getValue(GENDER)
    }
    set(value) {
        commitValue(GENDER, value)
    }
    var phone: String?
    get() {
        return getValue(PHONE)
    }
    set(value) {
        commitValue(PHONE, value)
    }

    fun clearToken() {
        DispenterApp.run {
            getContext().getSharedPreferences(
                SESSION_USER,
                Context.MODE_PRIVATE
            ).edit().clear().commit()
        }
    }

    private fun commitValue(tag: String, value: String?) {
        val memory = DispenterApp.getContext()
            .getSharedPreferences(SESSION_USER, Context.MODE_PRIVATE)

        memory
            .edit()
            .putString(tag, value)
            .apply()
    }

    private fun getValue(tag: String): String? {
        val memory = DispenterApp.getContext()
            .getSharedPreferences(SESSION_USER, Context.MODE_PRIVATE)

        return memory.getString(tag, "")
    }
}