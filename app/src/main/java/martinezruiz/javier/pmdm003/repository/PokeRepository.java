package martinezruiz.javier.pmdm003.repository;

import io.reactivex.rxjava3.core.Observable;
import martinezruiz.javier.pmdm003.models.Pokemon;
import martinezruiz.javier.pmdm003.models.PokemonList;

public interface PokeRepository {

    Observable<PokemonList> getPokemonList(int offset, int limit);
    Observable<Pokemon> getPokemon(Pokemon pokemon);


}
