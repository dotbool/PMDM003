package martinezruiz.javier.pmdm003.ui.pokedex;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import martinezruiz.javier.pmdm003.models.Pokemon;
import martinezruiz.javier.pmdm003.models.PokemonList;
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
        repo = new PokeRepository();

        if(!pokemonList.isInitialized()){
            repo.getPokemonList(0, 150, new Callback<PokemonList>() {
                @Override
                public void onResponse(Call<PokemonList> call, Response<PokemonList> response) {
                    if(response.isSuccessful()){
                        if(response.body()!=null) {
                            pokemonList.postValue(response.body().getPokemonList());
                        }
                    }
                }

                @Override
                public void onFailure(Call<PokemonList> call, Throwable throwable) {
                    System.out.println(throwable.getMessage());
                }
            });
        }
//        Pokemon p = new Pokemon();
//        p.setNombre("juan");
//        p.setPeso(2);
//        p.setIndice(2);
//        p.setAltura(222);
//        repoFire.addPokemon(p);


    }


    public LiveData<List<Pokemon>> getPokemons(){
        return pokemonList;
    }
    private MutableLiveData<List<Pokemon>> pokemonList;
    private PokeRepository repo;

    FirebaseFirestore fs = FirebaseFirestore.getInstance();



    /**
     * Cuando el botón capture es pulsado:
     * 1. Obtenemos lo que haya en la lista de pokemons
     * 2. Iteramos por la lista seleccionando los que tengan el state WANTED (seleccionados)
     * 3. LLamamos a la API por cada uno de ellos
     * 4. Recibimos un objeto pokemon pero no se ha reemplazado en la lista porque al hacerlo
     * se modifica el objeto completo (la lista) y el adapter sigue apuntando a la lista original.
     * Entonces al  llamar a Datachanged no se actualiza la vista
     */
    protected void capture(){


        List<Pokemon> pokemons = getPokemons().getValue();
        if(pokemons!=null){
            for(Pokemon p: pokemons){
                if(p.getState().equals(Pokemon.State.WANTED)){
                    disposablePokemon = repo.getPokemon(p.getNombre())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    onNext -> {
                                        p.setIndice(onNext.getIndice());
                                        p.setImgUrl(onNext.getImgUrl());
                                        p.setPeso(onNext.getPeso());
                                        p.setAltura(onNext.getAltura());
                                        p.setTypes(onNext.getTypes());
                                        p.setState(Pokemon.State.CAPTURED);
                                    },
                                    onError ->{
                                        System.out.println(onError);
                                    },
                                    ()->{
                                        pokemonList.postValue(pokemons);
                                        fs.collection("pokemon").document(p.getNombre())
                                                .set(p)
                                                .addOnSuccessListener(r->  Log.d(TAG, "DocumentSnapshot successfully written!"))
                                                .addOnFailureListener(e-> Log.e(TAG, "Error", e));

                                    }
                            );
                }

            }

        }

    }

    private String TAG = getClass().getName();

    Disposable disposablePokemon;
}
