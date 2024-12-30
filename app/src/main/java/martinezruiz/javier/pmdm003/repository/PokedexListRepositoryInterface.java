package martinezruiz.javier.pmdm003.repository;

import martinezruiz.javier.pmdm003.models.PokemonList;
import retrofit2.Callback;

public interface PokedexListRepositoryInterface {


    void getPokemonList(int offset, int limit, Callback<PokemonList> callback);


}
