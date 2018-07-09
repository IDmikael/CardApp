package com.idmikael.cardapp.ViewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class CardsViewModelFactory
@Inject constructor(
        private val cardsViewModel: CardsViewModel) : ViewModelProvider.Factory  {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CardsViewModel::class.java)) {
            return cardsViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}