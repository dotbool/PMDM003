package martinezruiz.javier.pmdm003.repository;

import io.reactivex.rxjava3.core.Observable;
import martinezruiz.javier.pmdm003.models.Pokemon;
import martinezruiz.javier.pmdm003.models.PokemonList;
import martinezruiz.javier.pmdm003.network.PokeApiClient;
import martinezruiz.javier.pmdm003.network.PokeApiService;
import retrofit2.Callback;

public class PokeRepository implements PokedexListRepositoryInterface {


    public PokeRepository() {
        pokeApiService = PokeApiClient.getClient().create(PokeApiService.class);

    }

    @Override
    public void getPokemonList(int offset, int limit, Callback<PokemonList> callback) {
        pokeApiService.getPokemonList(offset, limit).enqueue(callback);
    }

    public Observable<Pokemon> getPokemon(String name){
        return pokeApiService.getPokemon(name);
    }

    PokeApiService pokeApiService;


}
