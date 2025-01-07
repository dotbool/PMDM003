package martinezruiz.javier.pmdm003.repository;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import io.reactivex.rxjava3.core.Observable;
import martinezruiz.javier.pmdm003.MainActivity;
import martinezruiz.javier.pmdm003.models.Pokemon;


/**
 * Repositorio para las consultas a FireBase
 */
public class FireRepositoryImp implements FireRepository {


    private FireRepositoryImp(){

    }

    public static FireRepositoryImp getInstance(){
        if (INSTANCE == null){
            INSTANCE = new FireRepositoryImp();
        }
        return INSTANCE;
    }

    /**
     * Borra un objeto pokemon en FireBase
     * Este método siempre devuelve false porque la Task no está terminada.
     * Pero devuelve false en onSuccess. Se modificará en próximas versiones
     * para que la semántica de la devolución sea coherente
     * @param pokemon
     * @return
     */
    @Override
    public Observable<Boolean> deletePokemon(Pokemon pokemon) {
        path = "pok/"+userEmail+"/pokemon";
        return Observable.just(db.collection(path)
                .document(pokemon.getNombre())
                .delete().isSuccessful());
    }

    /**
     * Inserta un objeto pokemon en firebase
     * Como la librería de firebase devuelve un Task void, se ha transformado el tipo
     * devuelto para que encaje con el diseño
     * @param pokemon
     * @return
     */
    public Observable<Pokemon> insertPokemon(Pokemon pokemon){
        path = "pok/"+userEmail+"/pokemon";
        return Observable.fromCallable(()->{
            final Task<Void> task = db.collection(path)
                    .document(pokemon.getNombre())
                    .set(pokemon);
            Tasks.await(task);
            if(task.isSuccessful()){
                return pokemon;
            }
            else{
                return null;
            }
        });

    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    private static FireRepositoryImp INSTANCE;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userEmail;
    String path;

}
