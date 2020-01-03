package com.intech.activity.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.intech.R
import com.intech.activity.BarcodeActivity
import com.intech.activity.ui.base.BaseFragment
import com.intech.data.HomeRepository
import kotlinx.android.synthetic.main.fragment_home.*

class HomeScreen : BaseFragment<HomeScreen.State, HomeScreen.Presenter>() {

    class State : ViewModel()

    class Presenter(state: State,
                    view: HomeScreen,
                    repository: HomeRepository
    ) : BaseFragment.Presenter<State, HomeScreen, HomeRepository>(state, view, repository) {
        fun showToast(message: String) {
            view.activity {
                Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
            }
        }

        fun goToBarcodeScreen(type: String) {
            view.activity {
                val intent = Intent(it, BarcodeActivity::class.java).apply {
                    putExtra(BarcodeActivity.TYPE, type)
                }
                it.startActivity(intent)
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
    }

    private fun setOnActionButtonClicked() {
        coldWaterAction.setOnClickListener {
            presenter.goToBarcodeScreen(COLD_WATER)
        }
        normalWaterAction.setOnClickListener {
            presenter.goToBarcodeScreen(NORMAL_WATER)
        }
        tumblerAction.setOnClickListener {
            presenter.showToast("tumbler")
        }
    }

    companion object {
        private const val NORMAL_WATER = "normal"
        private const val COLD_WATER = "dingin"
    }
}