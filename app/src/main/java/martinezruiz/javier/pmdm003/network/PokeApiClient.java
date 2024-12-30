package martinezruiz.javier.pmdm003.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Hace peticiones a la API  y además convierte el JSON que obtiene en la respuesta en objetos
 * POJO para usarlos en la app
 */
public class PokeApiClient {

    private static String BASE_URL = "https://pokeapi.co/api/v2/";
    private static Retrofit retrofic;
    public static Retrofit getClient() {
        if(retrofic == null){
//
            retrofic = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return retrofic;
    }

}
