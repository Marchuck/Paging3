package pl.marczak.paging3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.rxjava3.disposables.Disposable
import pl.marczak.paging3.databinding.ListFragmentBinding

class ListFragment : Fragment() {

    private lateinit var binding: ListFragmentBinding

    private lateinit var viewModel: ListViewModel

    private var disposable: Disposable? = null

    private val pagedAdapter = PokedexAdapter()

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

        viewModel = ListViewModel(PokedexStreamProvider(PokedexPagingSource(PokeApiClient())))
        disposable =
            viewModel.observe()
                .subscribe({
                    binding.recyclerView.post { pagedAdapter.submitData(lifecycle, it) }
                }, {
                    Log.e("ListFragment", "onError $it", it)
                })

    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }
}
