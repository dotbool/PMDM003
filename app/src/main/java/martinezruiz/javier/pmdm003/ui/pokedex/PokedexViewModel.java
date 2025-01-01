package martinezruiz.javier.pmdm003.ui.pokedex;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import martinezruiz.javier.pmdm003.SharedPreferenceService;
import martinezruiz.javier.pmdm003.models.Pokemon;
import martinezruiz.javier.pmdm003.models.PokemonList;
import martinezruiz.javier.pmdm003.repository.FireRepository;
import martinezruiz.javier.pmdm003.repository.FireRepositoryImp;
import martinezruiz.javier.pmdm003.repository.PokeRepositoryImp;
import martinezruiz.javier.pmdm003.repository.PokeRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
//        disposePokemons.dispose();
    }

    public PokedexViewModel() {
        pokemonList = new MutableLiveData<>();
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
        if (pokemons != null) {
            for (Pokemon p : pokemons) {
                if (p.getState().equals(Pokemon.State.WANTED)) {
                    disposablePokemon = repo.getPokemon(p)
                            .concatMap(pokemon -> repoFire.insertPokemon(pokemon))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    next -> {
                                        System.out.println("next: " + next);
                                    },
                                    onError -> {
                                    },
                                    () -> {
                                        pokemonList.postValue(pokemons);
                                    });

                }
            }
        }
    }


    public void setCapturadosSp(ArrayList<String> capturadosSp) {

        this.capturadosSp = capturadosSp;
        if (!pokemonList.isInitialized()) {
            ArrayList<Pokemon> list = new ArrayList<>();
            repo.getPokemonList(0, 15)
                    .flatMap(pokemonList1 -> Observable.fromIterable(pokemonList1.getPokemonList()))
                    .concatMap(p-> {
                        if(capturadosSp.contains(p.getNombre())){
                            return repo.getPokemon(p.getNombre());
                        }
                        else {
                            return Observable.just(p);
                        }
                    })
                    .doOnNext(pokemon -> list.add(pokemon))
                    .subscribe(po-> System.out.println(po),
                            error->{},
                            ()-> pokemonList.postValue(list));








//            repo.getPokemonListCall(0, 15).enqueue(new Callback<PokemonList>() {
//                @Override
//                public void onResponse(Call<PokemonList> call, Response<PokemonList> response) {
//                    ArrayList<Pokemon> list = (response.body().getPokemonList()); //lista pokedex
//
//                    for (Pokemon p : list) {
//                        if (capturadosSp.contains(p.getNombre())) { //lista con capturados
//                            Disposable capt = repo.getPokemon(p.getNombre()).map(pok -> {
//
//                                p.setAltura(pok.getAltura());
//                                p.setImgUrl(pok.getImgUrl());
//
//                                return p;
//                            }).subscribe(next -> {
//                                    }, error -> {
//                                    },
//                                    () ->{
//                                         pokemonList.postValue(list);
//                                    });
//                        }
//                    }
//                    pokemonList.postValue(list);
//                }
//
//                @Override
//                public void onFailure(Call<PokemonList> call, Throwable throwable) {
//
//                }
//            });
        }
    }







//
//        if (!pokemonList.isInitialized()) {
//            repo.getPokemonList(0, 150, new PokeRepository.PokeRepositoryListener() {
//                @Override
//                public void onSuccess(PokemonList list) {
//                    pokemonList.postValue(list.getPokemonList());
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    System.out.println(e);
//
//                }
//            });
//        }


    public LiveData<List<Pokemon>> getPokemons() {
        return pokemonList;
    }

    private MutableLiveData<List<Pokemon>> pokemonList;
    private PokeRepositoryImp repo;
    private String TAG = getClass().getName();
    FireRepositoryImp repoFire = FireRepositoryImp.getInstance();
    Disposable disposablePokemon;
    private ArrayList<String> capturadosSp;
}


//        if(!pokemonList.isInitialized()){
//            repo.getPokemonList(0, 150, new Callback<PokemonList>() {
//                @Override
//                public void onResponse(Call<PokemonList> call, Response<PokemonList> response) {
//                    if(response.isSuccessful()){
//                        if(response.body()!=null) {
//                            pokemonList.postValue(response.body().getPokemonList());
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<PokemonList> call, Throwable throwable) {
//                    System.out.println(throwable.getMessage());
//                }
//            });
//        }

