package martinezruiz.javier.pmdm003.repository;

import com.google.android.gms.tasks.OnSuccessListener;

import io.reactivex.rxjava3.core.Observable;
import martinezruiz.javier.pmdm003.models.Pokemon;

public interface FireRepository {

    Observable<Boolean> insertPokemon(Pokemon pokemon);
    void deletePokemon(String nombre);

    interface FireRepositoryListener {
        void onSuccess(Pokemon pokemon);
        void onError(Exception e);
    }


}
