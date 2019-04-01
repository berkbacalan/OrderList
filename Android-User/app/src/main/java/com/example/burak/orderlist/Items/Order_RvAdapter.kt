package com.example.burak.orderlist.Items

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.burak.orderlist.Pages.ShoppingCart
import com.example.burak.orderlist.R
import kotlinx.android.synthetic.main.order_items.view.*
import kotlinx.android.synthetic.main.shoppingcart_items.view.*

class Order_RvAdapter(private val items: MutableList<String>)  : RecyclerView.Adapter<Order_RvAdapter.VH>(){
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

    fun removeAll(){
        items.clear()
        notifyDataSetChanged()
    }

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.order_items, parent, false)) {

        fun bind(name:String) = with(itemView) {
            orderRowName.text = name.split(":")[0]
            productPrice.text = name.split(":")[1]
            addProduct.setOnClickListener{
                ShoppingCart.rvAdapter.addItem(name.split(":")[0])
            }
        }

    }
}