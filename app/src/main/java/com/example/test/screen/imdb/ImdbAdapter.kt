package com.example.test.screen.imdb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.test.R
import com.example.test.databinding.ImdbItemBinding
import com.example.test.feature.entities.ImdbMovie

class ImdbAdapter(private val glide: RequestManager) : RecyclerView.Adapter<ImdbAdapter.ViewHolder>() {
    class ViewHolder(itemView: View, private val glide: RequestManager) : RecyclerView.ViewHolder(itemView) {
        private val binding = ImdbItemBinding.bind(itemView)

        fun bind(item: ImdbMovie) {
            binding.title.text = item.title ?: ""
            binding.year.text = item.year ?: ""
            binding.rating.text = item.imDbRating ?: ""

            item.thumbImage?.let {
                glide.load(it)
                    .fallback(R.drawable.bg_fallback)
                    .error(R.drawable.bg_error)
                    .placeholder(R.drawable.bg_placeholder)
                    .into(binding.image)
            } ?: glide.load(R.drawable.bg_placeholder).into(binding.image)
        }
    }

    var source: List<ImdbMovie> = emptyList()
        set(value) {
            if (field === value) {
                return
            }

            field = value
            notifyDataSetChanged()
        }

    init {
        setHasStableIds(false)
    }

    override fun getItemCount() = source.size
    override fun getItemId(position: Int) = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.imdb__item, parent, false),
        glide
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(source[position])
}
