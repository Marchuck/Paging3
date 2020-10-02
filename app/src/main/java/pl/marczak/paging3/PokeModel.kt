package pl.marczak.paging3

sealed class PokeModel {
    data class Header(val title: String) : PokeModel() {
        override fun getViewType() = VIEWTYPE_HEADER
    }

    data class Character(val pokemonEntry: PokemonEntry) : PokeModel() {
        override fun getViewType() = VIEWTYPE_POKE
    }

    abstract fun getViewType(): Int

    companion object {
        const val VIEWTYPE_NONE = 0
        const val VIEWTYPE_HEADER = 1
        const val VIEWTYPE_POKE = 2
    }
}
