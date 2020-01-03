package com.waterhub.activity.ui.base

interface Contract {
    interface View {
        fun renderLoading()
        fun renderNetworkError()
    }
}