package com.example.todoapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

//    val localeList: LocaleListCompat = LocaleListCompat.forLanguageTags("ru-Ru")
//    AppCompateDelegate.setApplicatio
//    val currentLocalName =  AppCompatDelegate.getApplicationLocales()[0]?.displayName

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as JobsApp).initBackgroundTodoWorker()
    }
}

