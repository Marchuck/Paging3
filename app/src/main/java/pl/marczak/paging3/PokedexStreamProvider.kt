package pl.marczak.paging3

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingData.Companion.insertSeparators
import androidx.paging.rxjava2.observable
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Observable

interface PagedListProvider {
    fun providePagedList(): Observable<PagingData<PokemonEntry>>
}

class PokedexStreamProvider constructor(
    private val pokedexPagingSource: PokedexPagingSource
) : PagedListProvider {

    private val generationsMap = mapOf(
        151 to "Generation II",
        251 to "Generation III",
        386 to "Generation IV",
        493 to "Generation V",
        649 to "Generation VI",
        721 to "Generation VII",
        809 to "Generation VIII"
    )

    private fun pagedListObservable() = Pager(
        config = PagingConfig(20),
        remoteMediator = null,
        pagingSourceFactory = { pokedexPagingSource }
    ).observable.map {
        insertSeparators(it) { before: PokemonEntry?, after: PokemonEntry? ->
            if (before == null) {
                return@insertSeparators PokemonEntry("Generation I", "")
            }
            val id = before.getPokeId()
            generationsMap[id]?.let { generationName ->
                return@insertSeparators PokemonEntry(generationName, "")
            }
            return@insertSeparators null
        }
    }

    override fun providePagedList(): Observable<PagingData<PokemonEntry>> {
        return RxJavaBridge.toV3Observable(pagedListObservable())
    }
}
