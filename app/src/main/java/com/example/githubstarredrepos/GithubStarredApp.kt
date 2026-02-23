package com.example.githubstarredrepos

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
@HiltAndroidApp
class GithubStarredApp: Application(){

    override fun onCreate() {
        super.onCreate()
        Log.d("GithubStarredApp", " Application started!")
    }
}
