package com.intech.activity.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.intech.R
import com.intech.activity.EditProfileActivity
import com.intech.activity.ui.base.BaseFragment
import com.intech.data.UserToken
import com.intech.data.UsersRepository
import kotlinx.android.synthetic.main.fragment_profile_screen.*

class ProfileScreen : BaseFragment<ProfileScreen.State, ProfileScreen.Presenter>() {

    class State : ViewModel() {
        val phoneNumber = MutableLiveData<String>()
        val birthDate = MutableLiveData<String>()
        val fullname = MutableLiveData<String>()
        val gender = MutableLiveData<String>()
        val email = MutableLiveData<String>()
    }

    class Presenter(state: State,
                    view: ProfileScreen,
                    repository: UsersRepository
    ) : BaseFragment.Presenter<State, ProfileScreen, UsersRepository>(state, view, repository) {
        fun getText() {
            state.apply {
                email.value = UserToken.email
                gender.value = UserToken.gender
                fullname.value = UserToken.userName
                birthDate.value = UserToken.birthdate
                phoneNumber.value = UserToken.phone
            }
        }

        fun goToEditProfileScreen() {
            view.activity {
                it.startActivity(Intent(it, EditProfileActivity::class.java))
            }
        }
    }

    override val state: State get() = ViewModelProviders.of(this).get(State::class.java)
    override val presenter: Presenter get() = Presenter(state, this, UsersRepository())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUserProfile()
        presenter.getText()
        setActionClickListener()
    }

    private fun setActionClickListener() {
        btnEdit.setOnClickListener {
            presenter.goToEditProfileScreen()
        }
    }

    private fun observeUserProfile() {
        state.apply {
            fullname.observe(
                this@ProfileScreen,
                Observer {
                    profileFullname.text = it
                }
            )
            email.observe(
                this@ProfileScreen,
                Observer {
                    fieldEmail.setText(it)
                }
            )
            phoneNumber.observe(
                this@ProfileScreen,
                Observer {
                    fieldPhone.setText(it)
                }
            )
            birthDate.observe(
                this@ProfileScreen,
                Observer {
                    fieldBirthDate.setText(it)
                }
            )
            gender.observe(
                this@ProfileScreen,
                Observer {
                    fieldGender.setText(it)
                }
            )
        }
    }
}