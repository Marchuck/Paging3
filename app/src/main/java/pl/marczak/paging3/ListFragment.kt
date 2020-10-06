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

    private var disposable: Disposable? = null

    private val pagedAdapter = PokedexAdapter()

    private val provider: PagedListProvider by lazy {
        PokedexStreamProvider(
            PokedexPagingSource(
                PokeApiClient()
            )
        )
    }

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

        disposable = provider.providePagedList().subscribe({
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
