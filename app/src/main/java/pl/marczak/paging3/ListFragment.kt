package pl.marczak.paging3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import pl.marczak.paging3.databinding.ListFragmentBinding

class ListFragment : Fragment() {

    private lateinit var binding: ListFragmentBinding

    private lateinit var viewModel: ListViewModel

    val pagedAdapter = PokedexPagedAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = pagedAdapter

        binding.progressBar.isVisible = true

        pagedAdapter.initialLoadEnd = {
            binding.progressBar.isVisible = false
        }

        viewModel = ListViewModel(PokedexStreamProvider(PokedexPagingSource(PokeApiClient())))
        val disposable = viewModel.observe()
            .subscribe({
                binding.recyclerView.post { pagedAdapter.submitData(lifecycle, it) }
            }, {
                Log.e("ListFragment", "onError $it", it)
            })
    }
}
