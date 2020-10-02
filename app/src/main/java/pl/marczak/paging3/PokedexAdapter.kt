package pl.marczak.paging3

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.marczak.paging3.PokeModel.Companion.VIEWTYPE_HEADER
import pl.marczak.paging3.PokeModel.Companion.VIEWTYPE_NONE
import pl.marczak.paging3.PokeModel.Companion.VIEWTYPE_POKE
import pl.marczak.paging3.databinding.ItemHeaderBinding
import pl.marczak.paging3.databinding.ItemLoadingBinding
import pl.marczak.paging3.databinding.ItemPokeBinding
import java.lang.IllegalStateException

class PokedexAdapter : PagingDataAdapter<PokeModel, BaseViewHolder>(DiffCallback()) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position)?.getViewType() ?: VIEWTYPE_NONE
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = getItem(position) ?: return
        when {
            item is PokeModel.Character && holder is BaseViewHolder.PokeViewHolder -> {
                holder.bind(item.pokemonEntry)
            }
            item is PokeModel.Header && holder is BaseViewHolder.HeaderHolder -> {
                holder.bind(item.title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            VIEWTYPE_NONE -> {
                val binding = ItemLoadingBinding.inflate(inflater, parent, false)
                return BaseViewHolder.ShimmerViewHolder(binding)
            }
            VIEWTYPE_HEADER -> {
                val binding = ItemHeaderBinding.inflate(inflater, parent, false)
                return BaseViewHolder.HeaderHolder(binding)
            }
            VIEWTYPE_POKE -> {
                val binding = ItemPokeBinding.inflate(inflater, parent, false)
                return BaseViewHolder.PokeViewHolder(binding)
            }
        }
        return notImplemented(viewType)
    }

    private fun notImplemented(viewType: Int): Nothing {
        throw IllegalStateException("no such viewType: $viewType")
    }
}

class DiffCallback : DiffUtil.ItemCallback<PokeModel>() {
    override fun areItemsTheSame(oldItem: PokeModel, newItem: PokeModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PokeModel, newItem: PokeModel): Boolean {
        return oldItem == newItem
    }
}

sealed class BaseViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    class PokeViewHolder(val binding: ItemPokeBinding) : BaseViewHolder(binding.root) {

        fun bind(entry: PokemonEntry) {
            binding.imageView.visibility = View.VISIBLE
            binding.imageView.loadPokeImage(entry.name)
            binding.textView.text = "#${String.format("%03d", entry.getPokeId())} ${entry.name}"
            binding.textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
        }
    }

    class HeaderHolder(val binding: ItemHeaderBinding) : BaseViewHolder(binding.root) {

        fun bind(header: String) {
            binding.root.text = header
        }
    }

    class ShimmerViewHolder(binding: ItemLoadingBinding) : BaseViewHolder(binding.root)
}
