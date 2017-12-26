package com.tejadroid.spiderview.base

import android.app.Activity

/**
 * Created by Tejas on 9/20/17.
 */

interface BaseView {

    fun onBack()

    fun getActivity(): Activity

    fun name(): String

}
