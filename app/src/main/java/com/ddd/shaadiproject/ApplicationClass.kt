package com.ddd.shaadiproject

import android.app.Application
import io.realm.Realm

class ApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}
