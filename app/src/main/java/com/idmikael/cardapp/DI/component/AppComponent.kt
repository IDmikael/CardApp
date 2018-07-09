package com.idmikael.cardapp.DI.component

import com.idmikael.cardapp.App
import com.idmikael.cardapp.DI.module.AppModule
import com.idmikael.cardapp.DI.module.BuilderModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [(AndroidInjectionModule::class), (BuilderModule::class), (AppModule::class)]
)
interface AppComponent {
    fun inject(app: App)
}