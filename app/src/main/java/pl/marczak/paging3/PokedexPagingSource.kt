package pl.marczak.paging3

import androidx.paging.PagingSource
import androidx.paging.rxjava2.RxPagingSource
import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.Single

typealias FetchResult = PagingSource.LoadResult<Int, PokeModel>

class PokedexPagingSource constructor(
    private val pokeApi: PokeApi
) : RxPagingSource<Int, PokeModel>() {

    override fun loadSingle(params: LoadParams<Int>): Single<FetchResult> {
        val key = params.key ?: 0

        return pokeApi.getPokedex(key, PAGE_SIZE)
            .map { response ->
                val result: FetchResult = LoadResult.Page(
                    data = response.results.map { PokeModel.Character(it) },
                    prevKey = if (key - PAGE_SIZE < 0) null else key - PAGE_SIZE,
                    nextKey = key + PAGE_SIZE,
                    itemsBefore = key,
                    itemsAfter = determineItemsAfter(key, response.count)
                )
                result
            }.asRxJava2Single()
    }

    private fun determineItemsAfter(key: Int, count: Int): Int {
        return (count - key).coerceAtLeast(0)
    }

    private fun <T> io.reactivex.rxjava3.core.Single<T>.asRxJava2Single(): Single<T> {
        return RxJavaBridge.toV2Single(this)
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
