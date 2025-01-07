package martinezruiz.javier.pmdm003.ui.pokedex;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import martinezruiz.javier.pmdm003.models.ErrorData;
import martinezruiz.javier.pmdm003.models.Pokemon;
import martinezruiz.javier.pmdm003.repository.FireRepositoryImp;
import martinezruiz.javier.pmdm003.repository.PokeRepositoryImp;


/**
 * Esta clase majena la lógica de la app alojando los datos que ésta usa y ofreciendo la oportunidad
 * de observarlos mediante el uso de LiveData. LiveData es una clase que destruye los objetos suscritos
 * cuando se destruye su alcance de forma que no se produzcan fugas de memoria porque existan
 * suscriptores sin utilidad.
 *  Los errores que devuelve esta clase se hacen a través de LiveData. Como los valores del viewmmodel
 *  persisten  y están asociados a la actividad, no queremos despachar nuevamente un error, así que
 *  sólo se despachan si no han sido previamente despachados. Es el fragmento el que torna la variable
 *  isHandle en true.
 */
public class PokedexViewModel extends ViewModel {



    public PokedexViewModel() {
        pokemonList = new MutableLiveData<>();
        errorData = new MutableLiveData<>();
        repo = new PokeRepositoryImp();
        repoFire = FireRepositoryImp.getInstance();

    }


    /**
     * Cuando el botón capture es pulsado:
     * 1. Obtenemos lo que haya en la lista de pokemons
     * 2. Iteramos por la lista seleccionando los que tengan el state WANTED (seleccionados)
     * 3. LLamamos a la API por cada uno de ellos
     * 4. Recibimos un objeto pokemon. Utilizamos los valores del pokemon recibido para setear
     * los valores del pokemon que hay en la lista observable que maneja el viewmodel.
     * Si alguna de las request fallase, concatMap sólo afecta a las emisiones successful qye reciba,
     * por lo que el que fallase no sería tratado y por lo tanto su estado no pasaría a CAPTURED.
     * 5. Una vez el objeto pokemon esté seteado llamamos al repo de Firebase y hacemos el
     * insert
     * 6. Se actualiza la lista pokemos que será observada en el fragmento y éste hará el trabajo
     * de guardarlo en el sharedpreferences
     */
    protected void capture() {

        repoFire.setUserEmail(userEmail); //mal diseño. Aspecto a mejorar
        List<Pokemon> pokemons = getPokemons().getValue(); //cogemos todos los pokemos
        if (pokemons != null) {
            for (Pokemon p : pokemons) {
                if (p.getState().equals(Pokemon.State.WANTED)) { //si el estado es wanted
                    disposablePokemon = repo.getPokemon(p) //llamamos a la API
                            .concatMap(pokemon -> repoFire.insertPokemon(pokemon)) //lo almacenamos en fs
                            .subscribeOn(Schedulers.io()) //las tareas de bd en otro thread
                            .observeOn(AndroidSchedulers.mainThread())// la vista en el main thread
                            .subscribe(
                                    next -> { //el pokemon que viene lo usamos para setear el pokemon de la lista
                                        p.setIndice(next.getIndice());
                                        p.setImgUrl(next.getImgUrl());
                                        p.setPeso(next.getPeso());
                                        p.setAltura(next.getAltura());
                                        p.setTypes(next.getTypes());
                                        p.setState(Pokemon.State.CAPTURED);
                                    },
                                    onError -> {
                                        if(errorData.getValue() !=null && !errorData.getValue().isHandled()) {
                                            errorData.postValue(new ErrorData("Error al procesar el pokemon"));
                                            Log.d("Error", onError.getMessage());
                                        }
                                    },
                                    () -> {
                                        pokemonList.postValue(pokemons);
                                    });

                }
            }
        }
    }

    /**
     *
     * @param pokemon
     */
    public void delete(Pokemon pokemon){
        repoFire.setUserEmail(userEmail);

        List<Pokemon> pokemonListR = pokemonList.getValue();
        disposableBorrados = repoFire.deletePokemon(pokemon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(onNext->{},
                        onError->{
                            if(errorData.getValue() !=null && !errorData.getValue().isHandled()) {
                                errorData.postValue(new ErrorData("Error al procesar el pokemon"));
                                Log.d("Error", onError.getMessage());
                            }
                        },
                        ()->{
                            pokemon.setState(Pokemon.State.FREE);
                            pokemonList.setValue(pokemonListR);
                });

    }

    /**
     * Esté método inicia la carga desde la API y es llamado desde el fragmentPokedex.
     * Se ha hecho así porque cuando la app se inicia debe cargar los pokemon que en anteriores
     * sesiones se hubiesen capturado. El sharedPreferences está ligado al contexto de la app
     * y el viewmodel no tiene acceso a este contexto y si he entendido bien, no debe tenerlo para
     * que no se produzcan fugas
     *
     * Si es la primera vez llama a la Api para desacagar los pokemons. Y ahora uno por uno compara
     * el nombre del pokemon con lo que haya en la lista de capturados. Si el nombre es igual
     * hace una llamada a fire base para descargar el pokemon y devuelve ese pokemon para que sea
     * añadido a la lista que es observada por el fragmento. Si no es igual el nombre pues se añade
     * a la lista lo que venga de la Api, es decir, el nombre y la url.
     * El trabajo con el sharedPreference se hace en el fragmento porque el sharedprefrence necesita
     * un contexto y el viewmodel no quiere saber nada de contextos ni vistas
     *
     * @param capturadosSp
     */
    public void setCapturadosSp(ArrayList<String> capturadosSp) {

        if (!pokemonList.isInitialized()) { //se llama al iniciar el fragmento por vez primera
            ArrayList<Pokemon> list = new ArrayList<>();
            disposableCapturados = repo.getPokemonList(0, 150)
                    .flatMap(pokemonList -> Observable.fromIterable(pokemonList.getPokemonList()))
                    .concatMap(p -> {
                        if (capturadosSp.contains(p.getNombre())) {
                            return repo.getPokemon(p);
                        }
                        else {
                            return Observable.just(p);
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(list::add)
                    .subscribe(po -> {
                            },
                            error -> {
                                if(errorData.getValue() !=null && !errorData.getValue().isHandled()) {
                                    errorData.postValue(new ErrorData("Error al procesar el pokemon"));
                                    Log.d("Error", error.getMessage());
                                }
                            },
                            () -> {
                            pokemonList.postValue(list);
                            });

        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposablePokemon!=null){
            disposablePokemon.dispose();
        }
        if(disposableBorrados!=null){
            disposableBorrados.dispose();
        }
        if(disposableCapturados!=null){
            disposableCapturados.dispose();
        }
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LiveData<List<Pokemon>> getPokemons() {
        return pokemonList;
    }

    public LiveData<ErrorData> getErrorData(){
        return errorData;
    }
    private MutableLiveData<List<Pokemon>> pokemonList;
    private MutableLiveData<ErrorData> errorData;
    private PokeRepositoryImp repo;
    FireRepositoryImp repoFire;
    String userEmail;
    Disposable disposablePokemon;
    Disposable disposableCapturados;
    Disposable disposableBorrados;

}



