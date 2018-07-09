package com.idmikael.cardapp.Activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import com.idmikael.cardapp.R
import com.idmikael.cardapp.ViewModel.AddNewCardViewModel
import com.idmikael.cardapp.ViewModel.AddNewCardViewModelFactory
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_add_new_card.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AddNewCardActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: AddNewCardViewModelFactory
    private lateinit var viewModel: AddNewCardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_card)

        AndroidInjection.inject(this)
        initToolbar()
        setDoneButtonEnabled(false)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(
                AddNewCardViewModel::class.java)

        initEditTextWatchers()

        btnAddNewCardDone.setOnClickListener {
            viewModel.insertCard()
            viewModel.cardsInsertion().observe(this, Observer<Boolean> {
                if (it == true) {
                    this.setResult(android.app.Activity.RESULT_OK)
                    this@AddNewCardActivity.finish()
                }
            })
        }
    }

    private fun initToolbar(){
        setSupportActionBar(tbAddNewCard)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        tbAddNewCard.setNavigationIcon(R.drawable.ic_arrow_left)

        tbAddNewCard.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initEditTextWatchers(){
        etAddNewCardNumber.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                viewModel.onCardNumberTextChanged(s?.toString())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .debounce(400, TimeUnit.MILLISECONDS)
                        .subscribe { showCardTypeImage(it) }

                viewModel.checkFields()
                        .subscribe {
                            setDoneButtonEnabled(it)
                        }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etAddNewCardName.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                viewModel.onCardNameChanged(s?.toString())

                viewModel.checkFields()
                        .subscribe {
                            setDoneButtonEnabled(it)
                        }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        etAddNewCardValid.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                viewModel.onCardValidnessChanged(s?.toString())

                viewModel.checkFields()
                        .subscribe {
                            setDoneButtonEnabled(it)
                        }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 1 && start == 1 && s?.get(s.length - 1) != '/') {
                    val txt = etAddNewCardValid.text.toString()
                    etAddNewCardValid.mask = "##/##"
                    etAddNewCardValid.setText(txt)
                }
                if (etAddNewCardValid.text.toString().length  == 0) {
                    etAddNewCardValid.mask = "###########"
                }
            }
        })

        etAddNewCardCVV.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                viewModel.onCardCVVChanged(s?.toString())

                viewModel.checkFields()
                        .subscribe {
                            setDoneButtonEnabled(it)
                        }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        //solve masked edittext on next action click issue
        etAddNewCardNumber.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener false
        }
        etAddNewCardValid.setOnEditorActionListener { v, actionId, event ->
            return@setOnEditorActionListener false
        }
    }

    private fun showCardTypeImage(type: Int){
        when(type){
            1 -> {
                val drawable = ContextCompat.getDrawable(this, R.drawable.ic_visa)
                etAddNewCardNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            }
            2 -> {
                val drawable = ContextCompat.getDrawable(this, R.drawable.ic_mastercard)
                etAddNewCardNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            }
            0 -> {
                etAddNewCardNumber.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
        }
    }

    private fun setDoneButtonEnabled(enabled: Boolean){
        if (enabled) {
            btnAddNewCardDone.alpha = 1.0f
            btnAddNewCardDone.isClickable = true
        }
        else {
            btnAddNewCardDone.alpha = 0.3f
            btnAddNewCardDone.isClickable = false
        }
    }
}
