package com.example.thalestestandroidapp.presentation.product_list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.thalestestandroidapp.R
import com.example.thalestestandroidapp.domain.models.Product

class ProductRecyclerViewAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            TODO("not implemented")
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            TODO("not implemented")
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ProductViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item_product,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProductViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Product>) {
        differ.submitList(list)
    }

    class ProductViewHolder(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Product) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(absoluteAdapterPosition, item)
            }

            itemView.findViewById<TextView>(R.id.card_title).text = item.name
            itemView.findViewById<TextView>(R.id.card_type).text = item.type.toString()
//            itemView.findViewById<TextView>(R.id.card_description).text = item.description
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Product)
    }
}