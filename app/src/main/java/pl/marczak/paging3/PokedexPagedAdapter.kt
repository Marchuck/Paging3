package pl.marczak.paging3

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import pl.marczak.paging3.databinding.ItemBookBinding

class PokedexPagedAdapter :
    PagingDataAdapter<PokemonEntry, ViewHolder>(DiffCallback()) {

    var initialLoadEnd: () -> Unit = {}
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let(holder::bind)
        initialLoadEnd()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}

class DiffCallback : DiffUtil.ItemCallback<PokemonEntry>() {
    override fun areItemsTheSame(oldItem: PokemonEntry, newItem: PokemonEntry): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PokemonEntry, newItem: PokemonEntry): Boolean {
        return oldItem == newItem
    }
}

class ViewHolder(private val binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(entry: PokemonEntry) {
        if (entry.url.isEmpty()) {
            binding.textView.text = entry.name
            binding.imageView.isVisible = false
        } else {
            binding.imageView.isVisible = true
            binding.imageView.loadPokeImage(entry.name)
            binding.textView.text = "#${entry.getPokeId()} ${entry.name}"
        }
    }
}
