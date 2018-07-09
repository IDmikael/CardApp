package com.idmikael.cardapp.DI.module

import com.idmikael.cardapp.Activities.AddNewCardActivity
import com.idmikael.cardapp.Activities.MyPaymentCardActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeMyPaymentCardActivity(): MyPaymentCardActivity

    @ContributesAndroidInjector
    abstract fun contributeAddNewCardActivity(): AddNewCardActivity
}