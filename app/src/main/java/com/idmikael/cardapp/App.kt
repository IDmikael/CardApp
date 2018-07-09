package com.idmikael.cardapp

import android.app.Activity
import android.app.Application
import com.idmikael.cardapp.DI.component.DaggerAppComponent
import com.idmikael.cardapp.DI.module.AppModule
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {
    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        @Suppress("DEPRECATION")
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
                .inject(this)

        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)

    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

}