package com.intech.activity.ui.base

/**
 * Created by YudaOktavian on 21-Sep-2019
 */
interface Contract {
    interface View {
        fun renderLoading()
        fun renderNetworkError()
    }
}