package com.example.bookapp

import android.app.Application
import com.airbnb.mvrx.Mavericks
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import org.maplibre.android.MapLibre

@HiltAndroidApp
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Mavericks.initialize(this)
        MapLibre.getInstance(this)
        Realm.init(this)
    }
}
