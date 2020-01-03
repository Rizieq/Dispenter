package com.intech.activity.ui.base

interface Contract {
    interface View {
        fun renderLoading()
        fun renderNetworkError()
    }
}