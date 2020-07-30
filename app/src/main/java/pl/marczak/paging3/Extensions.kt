package pl.marczak.paging3

import android.widget.ImageView
import coil.api.load


fun ImageView.loadPokeImage(name: String) {
    load("https://img.pokemondb.net/sprites/heartgold-soulsilver/normal/$name.png")
}


fun PokemonEntry.getPokeId(): Int {
    return url.replace("/", "").split("pokemon").last().toInt()
}
