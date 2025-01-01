package martinezruiz.javier.pmdm003.repository;

import io.reactivex.rxjava3.core.Observable;
import martinezruiz.javier.pmdm003.models.Pokemon;
import martinezruiz.javier.pmdm003.models.PokemonList;
import martinezruiz.javier.pmdm003.network.PokeApiClient;
import martinezruiz.javier.pmdm003.network.PokeApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokeRepositoryImp implements PokeRepository {


    public PokeRepositoryImp() {
        pokeApiService = PokeApiClient.getClient().create(PokeApiService.class);

    }

    public Call<PokemonList> getPokemonListCall(int offset, int limit) {
        return pokeApiService.getPokemonListCall(offset, limit);
    }



//    @Override
//    public void getPokemonList(int offset, int limit, PokeRepositoryListener listener) {
//
//        Callback<PokemonList> callback  = new Callback<>() {
//            @Override
//            public void onResponse(Call<PokemonList> call, Response<PokemonList> response) {
//                if (response.isSuccessful()) {
//                    if (response.body() != null) {
//                        listener.onSuccess(response.body());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PokemonList> call, Throwable throwable) {
//                listener.onError((Exception) throwable);
//            }
//        };
//
//        pokeApiService.getPokemonList(offset, limit).enqueue(callback);
//
//    }

    @Override
    public Observable<PokemonList> getPokemonList(int offset, int limit) {
        return pokeApiService.getPokemonList(offset, limit);
    }

    @Override
    public Observable<Pokemon> getPokemon(Pokemon pokemon) {

             return pokeApiService.getPokemon(pokemon.getNombre()).map(p->{

                 pokemon.setIndice(p.getIndice());
                 pokemon.setImgUrl(p.getImgUrl());
                 pokemon.setPeso(p.getPeso());
                 pokemon.setAltura(p.getAltura());
                 pokemon.setTypes(p.getTypes());
                 pokemon.setState(Pokemon.State.CAPTURED);

                 return pokemon;
             });

    }

    @Override
    public Observable<Pokemon> getPokemon(String name){
        return pokeApiService.getPokemon(name);
    }




    PokeApiService pokeApiService;


}
