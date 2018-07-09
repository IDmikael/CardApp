package com.idmikael.cardapp.ViewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.idmikael.cardapp.Model.Card
import com.idmikael.cardapp.Model.CardsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CardsViewModel @Inject constructor(
        private val cardsRepository: CardsRepository) : ViewModel() {

    var cardsResult: MutableLiveData<List<Card>> = MutableLiveData()
    var cardsError: MutableLiveData<String> = MutableLiveData()
    var cardsLoader: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var disposableObserver: DisposableObserver<List<Card>>

    fun cardsResult(): LiveData<List<Card>> {
        return cardsResult
    }

    fun cardsError(): LiveData<String> {
        return cardsError
    }

    fun cardsLoader(): LiveData<Boolean> {
        return cardsLoader
    }

    fun loadCards() {

        disposableObserver = object : DisposableObserver<List<Card>>() {
            override fun onComplete() {

            }

            override fun onNext(cards: List<Card>) {
                cardsResult.postValue(cards)
                cardsLoader.postValue(false)
            }

            override fun onError(e: Throwable) {
                cardsError.postValue(e.message)
                cardsLoader.postValue(false)
            }
        }

        cardsRepository.getCardsFromDb()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(disposableObserver)
    }

    fun disposeElements() {
        if (!disposableObserver.isDisposed) disposableObserver.dispose()
    }

}