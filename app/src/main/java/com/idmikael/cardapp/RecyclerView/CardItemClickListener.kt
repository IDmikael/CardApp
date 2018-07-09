package com.idmikael.cardapp.RecyclerView

interface CardItemClickListener {
    fun onEditItemClicked(position: Int, cardItem: CardItem)
}