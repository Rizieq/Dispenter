package com.waterhub.activity.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.waterhub.R
import com.waterhub.activity.AboutUsActivity
import com.waterhub.activity.BarcodeActivity
import com.waterhub.activity.TumblerActivity
import com.waterhub.activity.ui.base.BaseFragment
import com.waterhub.data.HomeRepository
import com.waterhub.data.UserToken
import com.waterhub.model.Banner
import com.waterhub.networking.Api
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Response
import java.lang.StringBuilder
import java.util.HashMap

class HomeScreen : BaseFragment<HomeScreen.State, HomeScreen.Presenter>() {

    class State : ViewModel()

   /* internal var compositeDisposable = CompositeDisposable()*/


    class Presenter(state: State,
                    view: HomeScreen,
                    repository: HomeRepository
    ) : BaseFragment.Presenter<State, HomeScreen, HomeRepository>(state, view, repository) {

        fun goToBarcodeScreen(type: String) {
            view.activity {
                val intent = Intent(it, BarcodeActivity::class.java).apply {
                    putExtra(BarcodeActivity.TYPE, type)
                }
                it.startActivity(intent)
            }
        }
        fun goToAboutUs(){
            view.activity{
                it.startActivity(Intent(it,AboutUsActivity::class.java))
            }
        }

        fun goToTumbler(){
            view.activity{
                it.startActivity(Intent(it,TumblerActivity::class.java))
            }

        }



    }

    override val state: State get() = ViewModelProviders.of(this).get(State::class.java)
    override val presenter: Presenter get() = Presenter(state, this, HomeRepository())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnActionButtonClicked()


        welcomeMessageTitle.setText(StringBuilder("Welcome, ").append(UserToken.userName ))
    }







    private fun setOnActionButtonClicked() {
        coldWaterAction.setOnClickListener {
            presenter.goToBarcodeScreen(COLD_WATER)
        }
        normalWaterAction.setOnClickListener {
            presenter.goToBarcodeScreen(NORMAL_WATER)
        }
        tumblerAction.setOnClickListener {
            presenter.goToTumbler()
        }
        aboutUsAction.setOnClickListener {
            presenter.goToAboutUs()
        }
    }




    companion object {
        private const val NORMAL_WATER = "normal"
        private const val COLD_WATER = "dingin"
        var EXTRA_GENDER = "extra_gender"
        var EXTRA_USERNAME = "extra_gender"
    }
}