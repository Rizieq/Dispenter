package com.waterhub.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.google.android.material.navigation.NavigationView
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.waterhub.R
import com.waterhub.activity.ui.home.HomeScreen
import com.waterhub.data.UserToken
import com.waterhub.model.Banner
import com.waterhub.networking.Api
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main_home.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Response
import java.util.HashMap


class MainHomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onStart() {
        super.onStart()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.waterhub.R.layout.activity_main_home)
        val toolbar: Toolbar = findViewById(com.waterhub.R.id.toolbar)
        setSupportActionBar(toolbar)


        getBannerImage()
        intent.getStringExtra(HomeScreen.EXTRA_USERNAME)
        intent.getStringExtra(HomeScreen.EXTRA_GENDER)

        Log.d("LOG_DATA ",HomeScreen.EXTRA_GENDER)

        // method for acces analytc
        val mixpanelAPI: MixpanelAPI = MixpanelAPI.getInstance(this, "19e9742aa9c2b512f7aed57e308eaf19")
        mixpanelAPI.track(UserToken.email,null)
        mixpanelAPI.flush()

        val drawerLayout: DrawerLayout = findViewById(com.waterhub.R.id.drawer_layout)
        val navView: NavigationView = findViewById(com.waterhub.R.id.nav_view)
        val navController = findNavController(com.waterhub.R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                com.waterhub.R.id.nav_home, com.waterhub.R.id.nav_gallery, com.waterhub.R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == com.waterhub.R.id.nav_slideshow) {
                UserToken.clearToken()
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        // set user email inside navigation drawer menu.
        val email: TextView = navView.getHeaderView(0).findViewById(R.id.emailUser)

        val waterhub: TextView = navView.getHeaderView(0).findViewById(R.id.logoHeader)
        val customfont = Typeface.createFromAsset(assets, "font/bluemub_.ttf")
        waterhub.typeface = customfont
        email.text = UserToken.email




    }


    private fun getBannerImage() {
        Api.getClient()
            .getBanners(UserToken.gender.toString())
            .enqueue(
                object: retrofit2.Callback<List<Banner>>{
                    override fun onFailure(call: Call<List<Banner>>, t: Throwable) {
                        Log.d("LOG_DATA_ERROR ",t.localizedMessage)
                    }

                    override fun onResponse(
                        call: Call<List<Banner>>,
                        response: Response<List<Banner>>
                    ) {
                        val banners = response.body()
                        Log.d("LOG_DATA_BENNER ",banners.toString())
                        displayImage(banners)
                    }

                }
            )
    }


    private fun displayImage(banners: List<Banner>?) {
        var bannerMap = HashMap<String,String>()
        for (item : Banner in banners!!)
            bannerMap.put(item.name!!, item.banner!!)

        for (name in bannerMap.keys)
        {
            val textSliderView: TextSliderView = TextSliderView(this)
            textSliderView.description(name)
                .image(bannerMap.get(name))
                .setScaleType(BaseSliderView.ScaleType.Fit)
            slider.addSlider(textSliderView)
        }

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(com.waterhub.R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object {
        const val ERROR = "ERROR"
    }

    internal var isBackButtonClicked = false

    override fun onBackPressed() {
        val drawer = drawer_layout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            if (isBackButtonClicked) {
                super.onBackPressed()
                return
            }
            this.isBackButtonClicked = true

            finishAffinity()
        }
    }
}
