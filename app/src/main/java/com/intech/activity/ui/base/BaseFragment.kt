package com.intech.activity.ui.base


import android.app.Activity
import androidx.fragment.app.Fragment

abstract class BaseFragment<S, P> : Fragment(), BaseFragmentContract<S, P>, MainPresenter {

    open class Presenter <S, V, R> (val state: S, val view: V, val repository: R)

    /**
     * It's safe closure lambda function. When screen
     * requires activity/context, closure lambda will make sure
     * you still have the context. if activity destroyed
     * it will safe, because the code inside closure lambda
     * not excecuted.
     */
    fun activity(act: (Activity) -> Unit) {
        activity?.let {
            act(it)
        }
    }

    /**
     * It's safe closure lambda function to get fragment. When screen
     * requires fragment/view, closure lambda will make sure
     * you still have the view. if view destroyed
     * it will safe, because the code inside closure lambda
     * not excecuted.
     */
    fun fragment(fragment: (Fragment) -> Unit) {
        if (isAdded) {
            fragment(this)
        }
    }

    /**
     * As default screen needs two type of rendering,
     * loading and error network. Just ignore when
     * the screen doesn't support this.
     */
    override fun renderLoading() {}
    override fun renderNetworkError() {}
}

interface BaseFragmentContract<S, P> {
    val state: S
    val presenter: P
}