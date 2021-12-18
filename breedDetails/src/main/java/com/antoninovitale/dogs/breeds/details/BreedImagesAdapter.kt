package com.antoninovitale.dogs.breeds.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.antoninovitale.dogs.breeds.details.databinding.BreedImageBinding
import com.squareup.picasso.Picasso
import javax.inject.Inject

private val diffCallback = object : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem
}

class BreedImagesAdapter @Inject constructor() :
    ListAdapter<String, BreedImagesAdapter.VH>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        VH(
            BreedImageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class VH(
        private val binding: BreedImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            Picasso.get()
                .load(item)
                .placeholder(R.drawable.placeholder_image_vector)
                .resize(200, 200)
                .onlyScaleDown()
                .centerCrop()
                .into(binding.breedImage)
        }
    }
}
