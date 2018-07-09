package com.idmikael.cardapp.ViewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.idmikael.cardapp.Model.Card
import com.idmikael.cardapp.Model.CardsRepository
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AddNewCardViewModel
@Inject constructor(private val cardsRepository: CardsRepository) : ViewModel()  {

    var cardsInsertion: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var completableObserver: CompletableObserver

    private var mCardNumber = ""
    private var mCardName = ""
    private var mCardValid = ""
    private var mCardCVV = ""

    fun cardsInsertion(): LiveData<Boolean> {
        return cardsInsertion
    }

    fun insertCard(){

        completableObserver = object : CompletableObserver{

            override fun onComplete() {
                cardsInsertion.postValue(true)
            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onError(e: Throwable) {

            }
        }

        Completable.fromCallable {
            cardsRepository.insertCard(Card(null, getCardNumber(), mCardName, getExpiryMonth(),
                    getExpiryYear(), mCardCVV.toInt(), false))
        }
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(completableObserver)

    }

    fun onCardNumberTextChanged(text: String?): Observable<Int>{
        mCardNumber = text ?: ""
        if (text?.get(0) == '4')
            return Observable.just(1)
        else if (text?.get(0) == '5')
            return Observable.just(2)
        else
            return Observable.just(0)
    }

    fun onCardNameChanged(name: String?){
        mCardName = name ?: ""
    }

    fun onCardValidnessChanged(validness: String?){
        mCardValid = validness ?: ""
    }

    fun onCardCVVChanged(cvv: String?){
        mCardCVV = cvv ?: ""
    }

    fun checkFields() : Observable<Boolean>{

        return Observables.combineLatest(
                validateCardNumber(),
                validateCardName(),
                validateCardValidness(),
                validateCardCVV(),
        {
            a,b,c,d -> a && b && c && d
        })

    }

    private fun validateCardNumber(): Observable<Boolean>{

        if (mCardNumber.length > 19 && mCardNumber[mCardNumber.length - 1].toString() == "X")
            mCardNumber = mCardNumber.replace("X", "")

        if (mCardNumber.contains("X", true))
            return Observable.just(false)
        else
            return Observable.just(true)
    }

    private fun validateCardName(): Observable<Boolean>{
        if (mCardName.isNotEmpty())
            return Observable.just(true)
        else
            return Observable.just(false)
    }

    private fun validateCardValidness(): Observable<Boolean>{
        if (mCardValid.isNotEmpty())
            return Observable.just(true)
        else
            return Observable.just(false)
    }

    private fun validateCardCVV(): Observable<Boolean>{
        if (mCardCVV.isNotEmpty())
            return Observable.just(true)
        else
            return Observable.just(false)
    }

    private fun getCardNumber(): String{
        return mCardNumber.replace("-", "")
    }

    private fun getExpiryMonth(): Int{
        return "${mCardValid[0]}${mCardValid[1]}".toInt()
    }

    private fun getExpiryYear(): Int{
        return "${mCardValid[mCardValid.length - 2]}${mCardValid[mCardValid.length - 1]}".toInt()
    }
}