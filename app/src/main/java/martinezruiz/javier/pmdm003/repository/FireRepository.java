package martinezruiz.javier.pmdm003.repository;

import io.reactivex.rxjava3.core.Observable;
import martinezruiz.javier.pmdm003.models.Pokemon;

public interface FireRepository {

    Observable<Pokemon> insertPokemon(Pokemon pokemon);
    Observable<Boolean> deletePokemon(Pokemon pokemon);




}
