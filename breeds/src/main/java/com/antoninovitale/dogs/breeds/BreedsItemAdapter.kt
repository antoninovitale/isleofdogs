package com.antoninovitale.dogs.breeds

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.antoninovitale.dogs.breeds.databinding.BreedsItemBinding
import javax.inject.Inject

private val diffCallback = object : DiffUtil.ItemCallback<BreedsItemModel>() {
    override fun areItemsTheSame(oldItem: BreedsItemModel, newItem: BreedsItemModel): Boolean =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: BreedsItemModel, newItem: BreedsItemModel): Boolean =
        oldItem == newItem
}

class BreedsItemAdapter @Inject constructor() :
    ListAdapter<BreedsItemModel, BreedsItemAdapter.VH>(diffCallback) {

    private lateinit var itemClickListener: ItemClickListener

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            BreedsItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            itemClickListener
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(
        private val binding: BreedsItemBinding,
        private val itemClickListener: ItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BreedsItemModel) {
            binding.breedName.text = item.name
            if (item.parent != null) {
                binding.breedParent.isVisible = true
                // This string could have been mapped and made available through the model.
                binding.breedParent.text =
                    binding.breedParent.context.getString(R.string.sub_breed_of, item.parent)
            } else {
                binding.breedParent.isVisible = false
            }
            binding.root.setOnClickListener {
                itemClickListener.onItemClicked(item)
            }
        }
    }

    fun interface ItemClickListener {

        fun onItemClicked(item: BreedsItemModel)
    }
}
