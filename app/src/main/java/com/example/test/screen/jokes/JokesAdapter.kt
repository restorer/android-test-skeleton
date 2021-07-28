package com.example.test.screen.jokes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.test.R
import com.example.test.databinding.JokesItemBinding
import com.example.test.feature.entities.JokesJoke
import java.util.*

class JokesAdapter(private val jokeIdFormat: String) : RecyclerView.Adapter<JokesAdapter.ViewHolder>() {
    class ViewHolder(itemView: View, private val jokeIdFormat: String) : RecyclerView.ViewHolder(itemView) {
        private val binding = JokesItemBinding.bind(itemView)

        fun bind(item: JokesJoke) {
            binding.jokeId.text = String.format(Locale.ROOT, jokeIdFormat, item.id)
            binding.jokeText.text = item.joke

            binding.categories.also {
                it.visibility = if (item.categories.isEmpty()) View.GONE else View.VISIBLE
                it.text = item.categories.joinToString { "#${it}" }
            }
        }
    }

    class DiffCallback(
        private val oldList: List<JokesJoke>,
        private val newList: List<JokesJoke>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }

    var source: List<JokesJoke> = emptyList()
        set(value) {
            if (field === value) {
                return
            }

            val diffResult = DiffUtil.calculateDiff(DiffCallback(field, value))
            field = value
            diffResult.dispatchUpdatesTo(this)
        }

    init {
        setHasStableIds(true)
    }

    override fun getItemCount() = source.size
    override fun getItemId(position: Int) = source[position].id

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.jokes__item, parent, false),
        jokeIdFormat
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(source[position])
}
