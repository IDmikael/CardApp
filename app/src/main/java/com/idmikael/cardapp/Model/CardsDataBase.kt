package com.idmikael.cardapp.Model

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(Card::class), version = 1)
abstract class CardsDataBase : RoomDatabase() {
    abstract fun cardDao(): CardDao
}