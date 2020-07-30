package pl.marczak.paging3

import android.annotation.SuppressLint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.marczak.paging3.databinding.ItemBookBinding

class PokedexAdapter : PagingDataAdapter<PokemonEntry, ViewHolder>(DiffCallback()) {

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
            binding.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f)
            binding.imageView.visibility = View.INVISIBLE
        } else {
            binding.imageView.visibility = View.VISIBLE
            binding.imageView.loadPokeImage(entry.name)
            binding.textView.text = "#${String.format("%03d", entry.getPokeId())} ${entry.name}"
            binding.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        }
    }
}
