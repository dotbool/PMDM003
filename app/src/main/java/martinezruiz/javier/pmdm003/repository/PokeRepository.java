package martinezruiz.javier.pmdm003.repository;

import io.reactivex.rxjava3.core.Observable;
import martinezruiz.javier.pmdm003.models.Pokemon;
import martinezruiz.javier.pmdm003.models.PokemonList;

public interface PokeRepository {

    void getPokemonList(int offset, int limit, PokeRepositoryListener listener);

    Observable<Pokemon> getPokemon(Pokemon pokemon);

    interface PokeRepositoryListener {
        void onSuccess(PokemonList list);

        void onError(Exception e);
    }

}
