package com.intech.activity

import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.widget.TextView
import android.widget.Toast
import com.intech.R
import com.intech.data.UserToken
import com.intech.networking.Api
import retrofit2.Call
import retrofit2.Response

class MainHomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onStart() {
        super.onStart()
        fetchUserData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.nav_slideshow) {
                UserToken.clearToken()
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        // set user email inside navigation drawer menu.
        val email: TextView = navView.getHeaderView(0).findViewById(R.id.emailUser)
        email.text = UserToken.email
    }

    private fun fetchUserData() {
        UserToken.email?.let {
            Api.usersService()
                .profile(it)
                .enqueue(
                    object : retrofit2.Callback<String> {
                        override fun onFailure(call: Call<String>, t: Throwable) {

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
            if (result != ERROR) {
                val splitResponse = response.body().toString().split(",")
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object {
        const val ERROR = "ERROR"
    }
}
