package pl.marczak.paging3

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.rxjava2.RxPagingSource
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.Single

typealias FetchResult = PagingSource.LoadResult<Int, PokemonEntry>

class PokedexPagingSource constructor(
    private val pokeApi: PokeApi
) : RxPagingSource<Int, PokemonEntry>() {


    override fun loadSingle(params: LoadParams<Int>): Single<FetchResult> {
        val key = params.key ?: 0

        return pokeApi.getPokedex(key, PAGE_SIZE)
            .map {
                val result: FetchResult = LoadResult.Page(it.results, null, key + PAGE_SIZE)
                result
            }.asRxJava2Single()
    }

    override val keyReuseSupported: Boolean
        get() = true

    fun <T> io.reactivex.rxjava3.core.Single<T>.asRxJava2Single(): Single<T> {
        return RxJavaBridge.toV2Single(this)
    }

    companion object {
        const val PAGE_SIZE = 20
    }

}
