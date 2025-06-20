package com.example.greenbytebazaar.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.greenbytebazaar.adapters.SpecialProductsAdapter.SpecialProductsViewHolder
import com.example.greenbytebazaar.data.Product
import com.example.greenbytebazaar.databinding.ProductRvItemBinding
import com.example.greenbytebazaar.databinding.SpecialRvItemBinding

class BestProductsAdapter: RecyclerView.Adapter<BestProductsAdapter.BestProductsViewHolder>() {

    inner class BestProductsViewHolder(private val binding: ProductRvItemBinding): RecyclerView.ViewHolder(binding.root){
           fun bind(product: Product){
               binding.apply {
                   Glide.with(itemView).load(product.images[0]).into(imgProduct)
                   product.offerPercentage?.let {
                       val remainingPricePercentage = 1f - it
                       val priceAfterOffer = remainingPricePercentage * product.price
                       tvNewPrice.text = "${String.format("%.2f",priceAfterOffer)} e.g"
                       tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

                   }
                   if (product.offerPercentage == null)
                       tvNewPrice.visibility = View.INVISIBLE
                   tvPrice.text = "${product.price} e.g"
                   tvName.text = product.name
               }

           }
        }
    private val diffCallback = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem==newItem
        }

    }
    val differ = AsyncListDiffer(this,diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductsViewHolder{
        return BestProductsViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }
    override fun onBindViewHolder(holder: BestProductsViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    var onClick:((Product)->Unit)?=null


}