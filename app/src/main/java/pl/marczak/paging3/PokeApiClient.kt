package pl.marczak.paging3

import com.google.gson.annotations.SerializedName
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class PokeApiClient : PokeApi {

    private val api by lazy {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApi::class.java)
    }

    override fun getPokedex(offset: Int, limit: Int): Single<PokedexResponse> {
        return api.getPokedex(offset, limit)
    }
}

interface PokeApi {
    @GET("api/v2/pokemon")
    fun getPokedex(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Single<PokedexResponse>
}

data class PokedexResponse(
    @SerializedName("next") val next: String,
    @SerializedName("previous") val previous: String,
    @SerializedName("results") val results: List<PokemonEntry>
)

data class PokemonEntry(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)
