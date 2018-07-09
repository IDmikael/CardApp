package com.idmikael.cardapp.Activities

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.idmikael.cardapp.Model.Card
import com.idmikael.cardapp.R
import com.idmikael.cardapp.RecyclerView.CardItem
import com.idmikael.cardapp.RecyclerView.CardItemClickListener
import com.idmikael.cardapp.RecyclerView.CardsListAdapter
import com.idmikael.cardapp.ViewModel.CardsViewModel
import com.idmikael.cardapp.ViewModel.CardsViewModelFactory
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_my_payment_card.*
import timber.log.Timber
import javax.inject.Inject

class MyPaymentCardActivity : AppCompatActivity() {

    @Inject
    lateinit var cardsViewModelFactory: CardsViewModelFactory
    private lateinit var cardsViewModel: CardsViewModel
    lateinit var adapter: CardsListAdapter
    var itemsList: ArrayList<CardItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_payment_card)

        AndroidInjection.inject(this)
        initializeToolbar()

        btnAddNewCard.setOnClickListener {
            val addNewCardActivity = Intent(this@MyPaymentCardActivity, AddNewCardActivity::class.java)
            startActivityForResult(addNewCardActivity, 0)
        }

        cardsViewModel = ViewModelProviders.of(this, cardsViewModelFactory).get(
                CardsViewModel::class.java)

        pbMain.visibility = View.VISIBLE
        loadData()
    }

    private fun initializeToolbar(){
        setSupportActionBar(tbMain)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        tbMain.setNavigationIcon(R.drawable.ic_arrow_left)

        tbMain.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initializeRecycler() {
        val layoutManager = LinearLayoutManager(this)
        rvMain.layoutManager = layoutManager
        rvMain.itemAnimator = DefaultItemAnimator()

        adapter = CardsListAdapter(itemsList, this, object : CardItemClickListener {
            override fun onEditItemClicked(position: Int, cardItem: CardItem) {
                //on edit button click
            }
        })

        rvMain.adapter = adapter
    }

    private fun loadData() {
        itemsList.clear()
        initializeRecycler()

        cardsViewModel.loadCards()

        cardsViewModel.cardsResult().observe(this,
                Observer<List<Card>> {
                    if (it != null) {
                        val position = adapter.itemCount
                        adapter.addCardsList(it)
                        rvMain.adapter = adapter
                    }
                })

        cardsViewModel.cardsError().observe(this, Observer<String> {
            if (it != null) {
                Timber.e("get cards error")
            }
        })

        cardsViewModel.cardsLoader().observe(this, Observer<Boolean> {
            if (it == false) pbMain.visibility = View.GONE
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            pbMain.visibility = View.VISIBLE
            loadData()
        }
    }

    override fun onDestroy() {
        cardsViewModel.disposeElements()
        super.onDestroy()
    }
}
