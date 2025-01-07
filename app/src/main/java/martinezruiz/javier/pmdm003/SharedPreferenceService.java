package martinezruiz.javier.pmdm003;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.common.util.JsonUtils;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import martinezruiz.javier.pmdm003.models.Pokemon;

public class SharedPreferenceService {


    public SharedPreferenceService(Context ctx, String email) {

        sp =  ctx.getSharedPreferences(ctx.getString(R.string.preference_file_capture)+ email ,
                Context.MODE_PRIVATE);
        editor = sp.edit();
        System.out.println(email+ "en el share");

    }

    public Map<String, ?> getAll(){
        sp.getAll().entrySet().forEach(s-> System.out.println(s));
        return sp.getAll();
    }

    public void escribirCapturado(String name, String state){
        editor.putString(name, state);
        editor.apply();

    }

    public void clear(){
        editor.clear().apply();
    }

    public void liberarCapturado(String name){
        editor.remove(name);
        editor.apply();

    }

    /**
     * Pokemons es la lista de pokemon descargada de la API cuando la app se abre.
     * Se busca en el archivo de preferencias las claves (nombres de pokemons) cuyo
     * valor sea CAPTURED y se setean en la lista recibida los pokemons correspopndientes
     * @param pokemons
     */
    public void setCaptures(List<Pokemon> pokemons){
        for (Pokemon p: pokemons){
            String state = sp.getString(p.getNombre(), "FREE");
            if(state.equals(Pokemon.State.CAPTURED.name())){
                p.setState(Pokemon.State.CAPTURED);
            }
        }
//        editor.clear().apply();
    }

    public ArrayList<String> iniciarCapturados(){
        ArrayList<String> captures = new ArrayList<>();
        sp.getAll().forEach((key, value) -> {
            if (value.equals(Pokemon.State.CAPTURED.name())){
                captures.add(key);
            }
        });
        return captures;
    }
    SharedPreferences sp;
    SharedPreferences.Editor editor;

}
