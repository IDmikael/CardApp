package com.idmikael.cardapp.Model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CardDao {
    @Query("SELECT * from cards")
    fun getAllCards(): Single<List<Card>>

    @Query("SELECT * FROM cards WHERE id=:id")
    fun getCardById(id: String): LiveData<Card>

    @Insert(onConflict = REPLACE)
    fun insert(card: Card)

    @Query("DELETE from cards WHERE id=:id")
    fun deleteCardById(id: String)
}