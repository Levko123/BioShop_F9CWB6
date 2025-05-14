package com.example.bioshop.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.bioshop.databinding.ItemProductBinding
import com.example.bioshop.model.Product
import com.example.bioshop.R
import androidx.appcompat.app.AlertDialog
import coil.load
import com.example.bioshop.data.ProductRepository


class ProductAdapter(
    private val onClick: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.VH>(ProductDiffCallback()) {

    inner class VH(private val binding: ItemProductBinding)
        : RecyclerView.ViewHolder(binding.root) {

        init {
            // hosszú nyomás → törlés
            binding.root.setOnLongClickListener {
                AlertDialog.Builder(it.context)
                    .setTitle("Termék törlése")
                    .setMessage("Biztos törlöd?")
                    .setPositiveButton("Igen") { _, _ ->
                        val id = getItem(bindingAdapterPosition).id
                        ProductRepository().delete(id)
                    }
                    .setNegativeButton("Mégse", null)
                    .show()
                true
            }
        }

        fun bind(item: Product) = with(binding) {
            tvName.text  = item.name
            tvPrice.text = String.format("%.0f Ft", item.price)
            ivThumb.load(item.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_placeholder)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_slide_in)
    }

}
