package martinezruiz.javier.pmdm003.ui.pokedex;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import martinezruiz.javier.pmdm003.models.Pokemon;
import martinezruiz.javier.pmdm003.repository.FireRepositoryImp;
import martinezruiz.javier.pmdm003.repository.PokeRepositoryImp;


/**
 * Esta clase majena la lógica de la app alojando los datos que ésta usa y ofreciendo la oportunidad
 * de observarlos mediante el uso de LiveData. LiveData es una clase que destruye los objetos suscritos
 * cuando se destruye su alcance de forma que no se produzcan fugas de memoria porque existan
 * suscriptores sin utilidad
 */
public class PokedexViewModel extends ViewModel {

    @Override
    protected void onCleared() {
        super.onCleared();
        disposablePokemon.dispose();
        disposableCapturados.dispose();
    }

    public PokedexViewModel() {
        pokemonList = new MutableLiveData<>();
        pokemonCaptured = new MutableLiveData<>();
        repo = new PokeRepositoryImp();


    }


    /**
     * Cuando el botón capture es pulsado:
     * 1. Obtenemos lo que haya en la lista de pokemons
     * 2. Iteramos por la lista seleccionando los que tengan el state WANTED (seleccionados)
     * 3. LLamamos a la API por cada uno de ellos
     * 4. Recibimos un objeto pokemon. Utilizamos los valores del pokemon recibido para setear
     * los valores del pokemon que hay en la lista observable que maneja el viewmodel. Si sustituyo
     * un pokemon por otro, la referencia
     * de la lista que los contiene cambia pero el adapter sigue apuntando a la lista original. Por eso
     * se ha hecho así, para no cambiar la referencia de la lista a la que apunta el adapter.
     * Si alguna de las request fallase, concatMap sólo afecta a las emisiones successful qye reciba,
     * por lo que el que fallase no sería tratado y por lo tanto su estado no pasaría a CAPTURED.
     * 5. Una vez el objeto pokemon esté seteado llamamos al repo de Firebase y hacemos el
     * insert pasándole un callback que en este caso lo que hace es guardar pares de clave (nombre
     * del pokemon) valor (estado del pokemon, por ejemplo 'bulbasaur: captured') en el sharedpreferences.
     * Esto se hace para que cuando la app se ponga en marcha, se haga el fecth inicial a la pokeAPi,
     * se intercepte esa request y se establezca el estado de lo que venga. De esta forma los pokemons
     * capturados seguirán estando capturados
     */
    protected void capture() {

        List<Pokemon> pokemons = getPokemons().getValue();
//        List<Pokemon> captured = getCaptured().getValue();
        if (pokemons != null) {
            for (Pokemon p : pokemons) {
                if (p.getState().equals(Pokemon.State.WANTED)) {
                    disposablePokemon = repo.getPokemon(p)
                            .concatMap(pokemon -> repoFire.insertPokemon(pokemon))
//                            .doOnNext(bol-> captured.add(p))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    next -> {

                                    },
                                    onError -> {
                                    },
                                    () -> {
                                        pokemonList.postValue(pokemons);
//                                        pokemonCaptured.postValue(captured);
                                    });

                }
            }
        }
    }


    public void delete(Pokemon pokemon){
        List<Pokemon> pokemonListR = pokemonList.getValue();
//        List<Pokemon> pokemonCapturedR = pokemonCaptured.getValue();
        pokemon.setState(Pokemon.State.FREE);
//        pokemonCapturedR.remove(pokemon);
        pokemonList.setValue(pokemonListR);
//        pokemonCaptured.setValue(pokemonCapturedR);

    }

    /**
     * Esté método inicia la carga desde la API y es llamado desde el fragmentPokedex.
     * Se ha hecho así porque cuando la app se inicia debe cargar los pokemon que en anteriores
     * sesiones se hubiesen capturado. El sharedPreferences está ligado al contexto de la app
     * y el viewmodel no tiene acceso a este contexto y si he entendido bien, no debe tenerlo para
     * que no se produzcan fugas
     *
     * @param capturadosSp
     */
    public void setCapturadosSp(ArrayList<String> capturadosSp) {

        this.capturadosSp = capturadosSp;
        if (!pokemonList.isInitialized()) {
            ArrayList<Pokemon> list = new ArrayList<>();
            ArrayList<Pokemon> capturedList = new ArrayList<>();
            disposableCapturados = repo.getPokemonList(0, 15)
                    .flatMap(pokemonList1 -> Observable.fromIterable(pokemonList1.getPokemonList()))
                    .concatMap(p -> {
                        if (capturadosSp.contains(p.getNombre())) {
                            return repo.getPokemon(p);
                        }
                        else {
                            return Observable.just(p);
                        }
                    }).subscribeOn(Schedulers.io())
                    .doOnNext( p->{
//                        if(p.getState().equals(Pokemon.State.CAPTURED)){
//                            capturedList.add(p);
//                        }
                        list.add(p);
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(po -> {
                            },
                            error -> {
                            },
                            () ->{
                                    pokemonList.postValue(list);
//                                    pokemonCaptured.postValue(capturedList);
                                });

        }
    }









    public LiveData<List<Pokemon>> getPokemons() {
        return pokemonList;
    }
    public LiveData<List<Pokemon>> getCaptured(){
        return pokemonCaptured;
    }
    private MutableLiveData<List<Pokemon>> pokemonCaptured;
    private MutableLiveData<List<Pokemon>> pokemonList;
    private PokeRepositoryImp repo;
    private String TAG = getClass().getName();
    FireRepositoryImp repoFire = FireRepositoryImp.getInstance();
    Disposable disposablePokemon;
    Disposable disposableCapturados;
    private ArrayList<String> capturadosSp;

    class LiveDataPokemon{

        public LiveDataPokemon() {
            pokemonList = new ArrayList<>();
            indices = new ArrayList<>();
        }

        public List<Integer> getIndices() {
            return indices;
        }

        public void setIndices(List<Integer> indices) {
            this.indices = indices;
        }

        public List<Pokemon> getPokemonList() {
            return pokemonList;
        }

        public void setPokemonList(List<Pokemon> pokemonList) {
            this.pokemonList = pokemonList;
        }

        List<Pokemon> pokemonList;
        List<Integer> indices;
    }
}



