package com.idmikael.cardapp.Model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "cards")
data class Card (
        @PrimaryKey(autoGenerate = true) val id: Int?,
        @ColumnInfo(name = "card_number") val cardNumber: String,
        @ColumnInfo(name = "card_name") val cardName: String,
        @ColumnInfo(name = "expiry_month") val expiryMonth: Int,
        @ColumnInfo(name = "expiry_year") val expiryYear: Int,
        @ColumnInfo(name = "card_cvv") val cvvCode: Int,
        @ColumnInfo(name = "is_default") val isDefault: Boolean
)