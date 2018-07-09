package com.idmikael.cardapp.RecyclerView

data class CardItem (val id: Int, val default: Boolean, val cardName: String, val cardNumber: String,
                     val expiryMonth: Int, val expiryYear: Int, val cvv: Int)