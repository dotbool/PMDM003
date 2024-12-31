package martinezruiz.javier.pmdm003.repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import io.reactivex.rxjava3.core.Observable;
import martinezruiz.javier.pmdm003.models.Pokemon;

public class FireRepositoryImp implements FireRepository {

    final static String COL = "pokemon";

    private FireRepositoryImp(){

    }


    public static FireRepositoryImp getInstance(){
        if (INSTANCE == null){
            INSTANCE = new FireRepositoryImp();
        }
        return INSTANCE;
    }


    @Override
    public void deletePokemon(String nombre) {

    }

    public Observable<Boolean> insertPokemon(Pokemon pokemon){

        return Observable.just(db.collection(COL)
                .document(pokemon.getNombre())
                .set(pokemon)
                .isSuccessful());

    }

    private static FireRepositoryImp INSTANCE;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


}
