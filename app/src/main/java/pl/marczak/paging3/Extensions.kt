package pl.marczak.paging3

import android.widget.ImageView
import coil.api.load
import coil.request.RequestDisposable


fun ImageView.loadPokeImage(name: String): RequestDisposable {
    return load("https://img.pokemondb.net/sprites/sword-shield/icon/$name.png")
}


fun PokemonEntry.getPokeId(): Int {
    return url.replace("/", "").split("pokemon").last().toInt()
}
