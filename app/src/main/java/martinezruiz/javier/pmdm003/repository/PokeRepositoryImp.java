package martinezruiz.javier.pmdm003.repository;

import io.reactivex.rxjava3.core.Observable;
import martinezruiz.javier.pmdm003.models.Pokemon;
import martinezruiz.javier.pmdm003.models.PokemonList;
import martinezruiz.javier.pmdm003.network.PokeApiClient;
import martinezruiz.javier.pmdm003.network.PokeApiService;

public class PokeRepositoryImp implements PokeRepository {


    public PokeRepositoryImp() {
        pokeApiService = PokeApiClient.getClient().create(PokeApiService.class);

    }

    @Override
    public Observable<PokemonList> getPokemonList(int offset, int limit) {
        return pokeApiService.getPokemonList(offset, limit);
    }

    @Override
    public Observable<Pokemon> getPokemon(Pokemon pokemon) {
             return pokeApiService.getPokemon(pokemon.getNombre());
    }


    PokeApiService pokeApiService;

}
