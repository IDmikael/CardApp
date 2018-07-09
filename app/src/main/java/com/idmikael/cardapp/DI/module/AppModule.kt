package com.idmikael.cardapp.DI.module

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import com.idmikael.cardapp.Model.CardDao
import com.idmikael.cardapp.Model.CardsDataBase
import com.idmikael.cardapp.ViewModel.AddNewCardViewModelFactory
import com.idmikael.cardapp.ViewModel.CardsViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val app: Application) {
    //for database migration
//    companion object {
//        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                // Change the table name to the correct one
//                database.execSQL("ALTER TABLE card RENAME TO cards")
//            }
//        }
//    }

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun provideCardsDatabase(app: Application): CardsDataBase = Room.databaseBuilder(app,
            CardsDataBase::class.java, "cards" /*database name*/)
            /*.addMigrations(MIGRATION_1_2)*/
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideCardsDao(
            database: CardsDataBase): CardDao = database.cardDao()

    @Provides
    @Singleton
    fun provideCardsViewModelFactory(
            factory: CardsViewModelFactory): ViewModelProvider.Factory = factory

    @Provides
    @Singleton
    fun provideAddNewCardViewModelFactory(
            factory: AddNewCardViewModelFactory): ViewModelProvider.Factory = factory

}