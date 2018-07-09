package com.idmikael.cardapp.Model

import io.reactivex.Completable
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

class CardsRepository
@Inject constructor(
        private val cardDao: CardDao) {
    //if there are some api requests, it will be here

    fun getCardsFromDb(): Observable<List<Card>> {
        return cardDao.getAllCards()
                .toObservable()
                .doOnNext {
                    Timber.e(it.size.toString())
                }
    }

    fun insertCard(card: Card){
        cardDao.insert(card)
    }
}