package pl.marczak.paging3

import android.annotation.SuppressLint
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.request.RequestDisposable
import pl.marczak.paging3.PokeModel.Companion.VIEWTYPE_HEADER
import pl.marczak.paging3.PokeModel.Companion.VIEWTYPE_NONE
import pl.marczak.paging3.PokeModel.Companion.VIEWTYPE_POKE
import pl.marczak.paging3.databinding.ItemHeaderBinding
import pl.marczak.paging3.databinding.ItemLoadingBinding
import pl.marczak.paging3.databinding.ItemPokeBinding
import java.lang.IllegalStateException

class PokedexAdapter : PagingDataAdapter<PokeModel, BaseViewHolder>(DiffCallback()) {

    private fun requireItem(position: Int): PokeModel {
        return getItem(position) ?: PokeModel.Placeholder
    }

    override fun getItemViewType(position: Int): Int {
        return requireItem(position).getViewType()
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = requireItem(position)
        when {
            item is PokeModel.Character && holder is BaseViewHolder.PokeViewHolder -> {
                holder.bind(item.pokemonEntry)
            }
            item is PokeModel.Header && holder is BaseViewHolder.HeaderHolder -> {
                holder.bind(item.title)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ) = BaseViewHolder.create(parent, viewType)
}

class DiffCallback : DiffUtil.ItemCallback<PokeModel>() {

    override fun areItemsTheSame(oldItem: PokeModel, newItem: PokeModel): Boolean {
        return when (oldItem) {
            is PokeModel.Character -> {
                newItem is PokeModel.Character && oldItem.pokemonEntry.name == newItem.pokemonEntry.name
            }
            is PokeModel.Header -> {
                newItem is PokeModel.Header && oldItem.title == newItem.title
            }
            PokeModel.Placeholder -> {
                newItem == PokeModel.Placeholder
            }
        }
    }

    override fun areContentsTheSame(oldItem: PokeModel, newItem: PokeModel): Boolean {
        return areItemsTheSame(oldItem, newItem)
    }
}

sealed class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    class PokeViewHolder(private val binding: ItemPokeBinding) : BaseViewHolder(binding.root) {

        private var requestDisposable: RequestDisposable? = null

        @SuppressLint("SetTextI18n")
        fun bind(entry: PokemonEntry) {
            binding.imageView.visibility = View.VISIBLE
            requestDisposable?.dispose()
            requestDisposable = binding.imageView.loadPokeImage(entry.name)
            binding.textView.text = "#${String.format("%03d", entry.getPokeId())} ${entry.name}"
        }
    }

    class HeaderHolder(private val binding: ItemHeaderBinding) : BaseViewHolder(binding.root) {

        fun bind(header: String) {
            binding.root.text = header
        }
    }

    class ShimmerViewHolder(binding: ItemLoadingBinding) : BaseViewHolder(binding.root)

    companion object {
        fun create(parent: ViewGroup, viewType: Int): BaseViewHolder {
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
            throw IllegalStateException("viewType: $viewType not supported")
        }
    }
}
