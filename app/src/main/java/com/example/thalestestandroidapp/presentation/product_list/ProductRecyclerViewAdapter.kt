package com.example.thalestestandroidapp.presentation.product_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.thalestestandroidapp.R
import com.example.thalestestandroidapp.domain.models.Product
import com.example.thalestestandroidapp.presentation.utils.toFormattedPrice
import timber.log.Timber

class ProductRecyclerViewAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)


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
            itemView.findViewById<TextView>(R.id.card_description).text = item.description
            itemView.findViewById<TextView>(R.id.card_price).text = buildString {
                append("$")
                append(item.price.toFormattedPrice())
            }
            itemView.findViewById<ImageView>(R.id.card_image).apply {
                load(item.imageUrl) {
                    crossfade(true)
                    error(R.drawable.cannot_load_image)
                    listener(
                        onError = { _, _ ->
                            imageTintList = ContextCompat.getColorStateList(
                                context,
                                R.color.md_theme_onPrimaryContainer
                            )
                            background = ContextCompat.getDrawable(context, R.color.md_theme_inversePrimary_highContrast)
                        }
                    )
                }

            }

        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Product)
    }
}