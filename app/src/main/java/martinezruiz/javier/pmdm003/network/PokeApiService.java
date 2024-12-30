package martinezruiz.javier.pmdm003.network;

import io.reactivex.rxjava3.core.Observable;
import martinezruiz.javier.pmdm003.models.Pokemon;
import martinezruiz.javier.pmdm003.models.PokemonList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Aloja las llamadas que se harán a la API a través del servicio que ofrece Retrofit
 * EL parámetro de Call hace referencia al tipo de dato que la llamada espera
 */
public interface PokeApiService {



    @GET("pokemon")
    Call<PokemonList> getPokemonList(@Query("offset") int offset, @Query("limit") int limit);

    @GET("pokemon/{name}")
    Observable<Pokemon> getPokemon(@Path("name") String name);
//    Call<Pokemon> getPokemons(@Path("name") String name);




}
