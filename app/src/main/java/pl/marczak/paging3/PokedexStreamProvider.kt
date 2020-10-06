package pl.marczak.paging3

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingData.Companion.insertSeparators
import androidx.paging.rxjava2.observable
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.core.Observable
import pl.marczak.paging3.PokedexPagingSource.Companion.PAGE_SIZE

interface PagedListProvider {
    fun providePagedList(): Observable<PagingData<PokeModel>>
}

class PokedexStreamProvider constructor(
    private val pokedexPagingSource: PokedexPagingSource
) : PagedListProvider {

    private val generationsStartInfo = mapOf(
        151 to "Generation II",
        251 to "Generation III",
        386 to "Generation IV",
        493 to "Generation V",
        649 to "Generation VI",
        721 to "Generation VII",
        809 to "Generation VIII"
    )

    private fun pagedListObservable() = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = true),
        remoteMediator = null,
        pagingSourceFactory = { pokedexPagingSource }
    ).observable.map { it.insertGenerationHeaders() }

    private fun PagingData<PokeModel>.insertGenerationHeaders(): PagingData<PokeModel> {
        return insertSeparators(this) { before: PokeModel?, _: PokeModel? ->
            if (before == null) {
                return@insertSeparators PokeModel.Header("Generation I")
            }
            require(before is PokeModel.Character)
            val id = before.pokemonEntry.getPokeId()
            generationsStartInfo[id]?.let { generationName ->
                return@insertSeparators PokeModel.Header(generationName)
            }
            return@insertSeparators null
        }
    }

    override fun providePagedList(): Observable<PagingData<PokeModel>> {
        return RxJavaBridge.toV3Observable(pagedListObservable())
    }
}
