package com.example.weatherapp

import android.app.Application
import androidx.activity.ComponentActivity
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApp: Application() {
    private var mCurrentActivity: ComponentActivity? = null

    fun getCurrentActivity(): ComponentActivity {
        if (mCurrentActivity == null) throw NullPointerException()

        return mCurrentActivity!!
    }

    fun setCurrentActivity(mCurrentActivity: ComponentActivity?) {
        this.mCurrentActivity = mCurrentActivity
    }

    fun checkAndClearCurrentActivity(callerActivity: ComponentActivity) {
        if (mCurrentActivity != null && callerActivity == mCurrentActivity)
            setCurrentActivity(null)
    }
}