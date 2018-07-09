package com.idmikael.cardapp.RecyclerView

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableContainer
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import com.idmikael.cardapp.Model.Card
import com.idmikael.cardapp.R
import kotlinx.android.synthetic.main.item_card.view.*

class CardsListAdapter(internal var items: ArrayList<CardItem>,
                       var context: Context,
                       var cardItemClickListener: CardItemClickListener
)  : RecyclerView.Adapter<CardsListAdapter.CardItemViewHolder>()  {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CardItemViewHolder, position: Int) {
        val tip = items[position]
        holder.bindItem(tip, context, cardItemClickListener)
    }

    fun addCardsList(list: List<Card>){
        items.clear()
        list.forEach {
            items.add(CardItem(it.id!!, it.isDefault, it.cardName, it.cardNumber, it.expiryMonth, it.expiryYear, it.cvvCode))
        }
        notifyItemRangeInserted(0, items.size)
    }

    fun clearItemsList(){
        items.clear()
        notifyDataSetChanged()
    }

    fun addItem(item: CardItem){
        items.add(item)
        this.notifyItemInserted(items.size)
    }

    fun removeItem(position: Int){
        items.removeAt(position)
        this.notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardItemViewHolder {
        val view = View.inflate(parent.context, R.layout.item_card, null)
        return CardItemViewHolder(view)
    }

    class CardItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(item: CardItem, context: Context, listener: CardItemClickListener) {

            //default card logic =)
            var default = false
            if (item.id == 0)
                default = true

            if (default) {
                itemView.tvItemDefaultCard.visibility = View.VISIBLE
                val drawable = ContextCompat.getDrawable(context, R.drawable.ic_tick)
                DrawableCompat.setTint(drawable!!, ContextCompat.getColor(context, R.color.orange))
                itemView.tvItemDefaultCard.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            }
            else
                itemView.tvItemDefaultCard.visibility = View.GONE

            itemView.tvItemCardName.text = item.cardName

            if (item.cardNumber.startsWith("5", true)) {
                itemView.ivItemCardImage.setImageResource(R.drawable.ic_mastercard)
                itemView.ivItemCardImage.visibility = View.VISIBLE
            }
            else if (item.cardNumber.startsWith("4", true)) {
                itemView.ivItemCardImage.setImageResource(R.drawable.ic_visa)
                itemView.ivItemCardImage.visibility = View.VISIBLE
            }
            else
                itemView.ivItemCardImage.visibility = View.GONE

            getCardNumber(item.cardNumber)

            itemView.tvItemExpire.text = "${item.expiryMonth}/${item.expiryYear}"

            itemView.ibItemEdit.setOnClickListener({ listener.onEditItemClicked(this.adapterPosition, item) })
        }

        fun getCardNumber(number: String){
            var cardNumber = ""

            for (i in 0..3){
                cardNumber += number.get(i)
            }

            cardNumber += " - xxxx - xxxx - "

            for (i in number.length - 4 until number.length){
                cardNumber += number.get(i)
            }

            val spannable = SpannableString(cardNumber)
            spannable.setSpan(ForegroundColorSpan(Color.GRAY), 4, 21, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            itemView.tvItemCardNumber.text = spannable
        }
    }
}