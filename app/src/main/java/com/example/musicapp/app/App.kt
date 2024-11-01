package com.example.musicapp.app

import android.app.Application
import com.example.musicapp.di.appModule
import com.example.musicapp.di.dataModule
import com.example.musicapp.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(appModule, dataModule, domainModule)
        }
    }
}