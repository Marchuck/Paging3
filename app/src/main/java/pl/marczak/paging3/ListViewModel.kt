package pl.marczak.paging3

import androidx.lifecycle.ViewModel


class ListViewModel constructor(
    private val pokedexStreamProvider: PokedexStreamProvider
) : ViewModel() {

    fun observe() = pokedexStreamProvider.providePagedList()

}
