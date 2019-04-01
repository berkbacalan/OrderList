package com.example.burak.orderlist.Items

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.burak.orderlist.R
import kotlinx.android.synthetic.main.shoppingcart_items.view.*

class ShoppingCart_RvAdapter(private val items: MutableList<String>) : RecyclerView.Adapter<ShoppingCart_RvAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun addItem(name: String) {
        items.add(name)
        notifyItemInserted(items.size)
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.shoppingcart_items, parent, false)) {

        fun bind(name: String) = with(itemView) {
            rowName.text = name

            increaseNumber.setOnClickListener{
                var order_amount: Int = amount.text.toString().toInt()
                order_amount = order_amount + 1
                amount.text = "" + order_amount
            }
            decreaseNumber.setOnClickListener{
                var order_amount: Int = amount.text.toString().toInt()
                order_amount = order_amount - 1
                amount.text = "" + order_amount
            }
        }
    }
}